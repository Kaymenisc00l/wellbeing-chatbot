alert("chat.js loaded");

let conversationStage = "EMOTION";
let lastEmotion = null;
let lastCategory = null;

let messages, inputEl, sendBtn;
let cachedTips = [], cachedLinks = [];

// ---------- UI ----------
function addMessage(sender, text) {
const msg = document.createElement("div");
msg.classList.add("message", sender === "You" ? "user-message" : "bot-message");
msg.innerText = text;
messages.appendChild(msg);
messages.scrollTop = messages.scrollHeight;
}

function typeBotMessage(text) {
const msg = document.createElement("div");
msg.classList.add("message", "bot-message");
messages.appendChild(msg);

let index = 0;
const interval = setInterval(() => {
msg.textContent += text.charAt(index++);
if (index >= text.length) clearInterval(interval);
messages.scrollTop = messages.scrollHeight;
}, 20);
}

function addLinkMessage(url) {
const msg = document.createElement("div");
msg.classList.add("message", "bot-message");

const a = document.createElement("a");
a.href = url;
a.textContent = url;
a.target = "_blank";
a.rel = "noopener noreferrer";

msg.appendChild(a);
messages.appendChild(msg);
messages.scrollTop = messages.scrollHeight;
}

function showOptions(options) {
  const optionsBar = document.getElementById("optionsBar");
  if (!optionsBar) return;

  // clear old buttons
  optionsBar.innerHTML = "";

  options.forEach(opt => {
    const btn = document.createElement("button");
    btn.type = "button";
    btn.textContent = opt;
    btn.addEventListener("click", () => handleUserInput(opt));
    optionsBar.appendChild(btn);
  });

  // keep view scrolled to bottom
  messages.scrollTop = messages.scrollHeight;
}

// ---------- Mood ----------
async function saveMood(emotion, category) {
const res = await fetch("/api/mood/save", {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify({ emotion, category })
});

if (!res.ok) throw new Error("Failed to save mood");
}

async function loadMoodHistory() {
try {
const moodList = document.getElementById("moodList");
if (!moodList) return;

const res = await fetch("/api/mood/all");
const moods = await res.json();

moodList.innerHTML = "";

moods.forEach(mood => {
const row = document.createElement("div");
row.classList.add("mood-item");

const text = document.createElement("span");
text.textContent = `${mood.date} - ${mood.emotion} (${mood.category})`;

const del = document.createElement("button");
del.textContent = "x";
del.classList.add("delete-mood-btn");

del.onclick = async () => {
  const id = mood.id; // or mood.moodId depending on your JSON
  if (id == null) {
    console.error("Mood id missing:", mood);
    alert("Could not delete: mood id missing (check API response).");
    return;
  }
  await deleteMood(id);
  await loadMoodHistory();
};

row.appendChild(text);
row.appendChild(del);
moodList.appendChild(row);
});
} catch (e) {
console.error("loadMoodHistory failed", e);
}
}

async function deleteMood(id) {
  console.log("Deleting mood id:", id);

  const res = await fetch(`/api/mood/${id}`, { method: "DELETE" });

  console.log("Delete status:", res.status);

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    console.error("Delete failed:", text);
    throw new Error("Failed to delete mood");
  }
}

// ---------- Chat ----------
async function handleUserInput(input) {
const normalized = input.toLowerCase().trim();
addMessage("You", input);

if (conversationStage === "EMOTION") {
lastEmotion = input;

const res = await fetch("/api/chat/feeling", {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify({ feeling: input })
});

const data = await res.json();
typeBotMessage(data.message);
showOptions(data.nextOptions);

conversationStage = "CATEGORY";
return;
}

if (conversationStage === "CATEGORY") {
if (normalized.includes("restart")) {
conversationStage = "EMOTION";
typeBotMessage("Let’s start again 😊 How are you feeling?");
showOptions(["stressed", "anxious", "sad", "tired"]);
return;
}

lastCategory = input;

const res = await fetch("/api/chat/category", {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify({ category: input })
});

const data = await res.json();
cachedTips = data.tips || [];
cachedLinks = data.links || [];

typeBotMessage(data.message + " What would you like to do next?");
showOptions(["View tips", "View support links", "Save mood", "Restart chat"]);

conversationStage = "CHOICE";
return;
}

if (conversationStage === "CHOICE") {
if (normalized.includes("tip")) {
cachedTips.forEach(t => typeBotMessage("• " + t));
return;
}

if (normalized.includes("support")) {
cachedLinks.forEach(l => addLinkMessage(l));
return;
}

if (normalized.includes("save")) {
await saveMood(lastEmotion, lastCategory);
typeBotMessage("Saved ✅ (see Mood History)");
await loadMoodHistory();
return;
}

if (normalized.includes("restart")) {
conversationStage = "EMOTION";
typeBotMessage("Let’s start again 😊 How are you feeling?");
showOptions(["stressed", "anxious", "sad", "tired"]);
return;
}

typeBotMessage("Please choose one of the options.");
}
}

// ---------- DOM ----------
document.addEventListener("DOMContentLoaded", () => {
messages = document.getElementById("messages");
inputEl = document.getElementById("userInput");
sendBtn = document.getElementById("sendBtn");

typeBotMessage("DISCLAIMER: This is general wellbeing info only. Not medical advice. If you are in danger, contact emergency services / NHS 111.");
addMessage("Bot", "Hi 👋 Type one word that best describes how you're feeling, or choose below.");
showOptions(["stressed", "anxious", "sad", "tired"]);

sendBtn.addEventListener("click", () => {
const text = inputEl.value.trim();
if (!text) return;
inputEl.value = "";
handleUserInput(text);
});

inputEl.addEventListener("keydown", (e) => {
if (e.key === "Enter") {
e.preventDefault();
const text = inputEl.value.trim();
if (!text) return;
inputEl.value = "";
handleUserInput(text);
}
});
const sidebar = document.getElementById("sidebar");
const toggle = document.getElementById("toggleSidebar");
if (sidebar && toggle) {
toggle.addEventListener("click", () => sidebar.classList.toggle("collapsed"));
}

const addMoodBtn = document.getElementById("addMoodBtn");
if (addMoodBtn) {
addMoodBtn.addEventListener("click", async () => {
const e = document.getElementById("manualEmotion")?.value.trim();
const c = document.getElementById("manualCategory")?.value.trim();
if (!e || !c) return alert("Enter both emotion + category");
await saveMood(e, c);
document.getElementById("manualEmotion").value = "";
document.getElementById("manualCategory").value = "";
await loadMoodHistory();
});
}

loadMoodHistory();
});

