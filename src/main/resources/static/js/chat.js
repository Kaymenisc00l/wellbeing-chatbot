alert("chat.js loaded");

let conversationStage = "EMOTION";
let lastEmotion = "";


let messages;
let inputEl;
let sendBtn;
let cachedTips = [];
let cachedLinks = [];


// ---------------- UI ----------------



function addMessage(sender, text) {

  const msg = document.createElement("div");
  msg.classList.add("message");

  if (sender === "You") {
    msg.classList.add("user-message");
  } else {
    msg.classList.add("bot-message");
  }

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
    msg.textContent += text.charAt(index);
    index++;

    if (index >= text.length) {
      clearInterval(interval);
    }

    messages.scrollTop = messages.scrollHeight;
  }, 25);
}

function addLinkMessage(url) {

  const msg = document.createElement("div");
  msg.classList.add("message", "bot-message");

  const link = document.createElement("a");
  link.href = url;
  link.textContent = url;
  link.target = "_blank";
  link.rel = "noopener noreferrer";

  msg.appendChild(link);
  messages.appendChild(msg);

  messages.scrollTop = messages.scrollHeight;
}


function showOptions(options) {
  const existing = document.getElementById("options");
  if (existing) existing.remove();

  const optionsDiv = document.createElement("div");
  optionsDiv.id = "options";

  options.forEach(opt => {
    const btn = document.createElement("button");
    btn.textContent = opt;
    btn.onclick = () => handleUserInput(opt);
    optionsDiv.appendChild(btn);
  });

  messages.appendChild(optionsDiv);
}

async function loadMoodHistory(){
    try{
      const res = await fetch("/api/mood/all");
      const moods = await res.json();

      const moodList = document.getElementById("moodList");
      moodList.innerHTML = "";

      moods.forEach(mood =>{
        const item = document.createElement("p");
        item.textContent = `${mood.date} - ${mood.emotion} (${mood.category})`;
        moodList.appendChild(item);

      });

    }catch (err){
      console.error("Failed to load moods", err);

    }
  }

// ---------------- CHAT LOGIC ----------------

async function handleUserInput(input) {
  const normalised = input.toLowerCase().trim();


  addMessage("You", input);


  // -------- EMOTION --------
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

  // -------- CATEGORY --------
 if (conversationStage === "CATEGORY") {

  if (normalised.includes("restart")) {
    conversationStage = "EMOTION";
    typeBotMessage("Let’s start again 😊 How are you feeling?");
    showOptions(["stressed", "anxious", "sad", "tired"]);
    return;
  }

  const res = await fetch("/api/chat/category", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ category: input })
  });

  const data = await res.json();

  cachedTips = data.tips || [];
  cachedLinks = data.links || [];

  // ⭐ SAVE MOOD HERE
  await fetch("/api/mood/save", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      emotion: lastEmotion,
      category: input
    })
  });

  loadMoodHistory();

  typeBotMessage(data.message + " What would you like to view next?");
  showOptions(["View tips", "View support links", "Restart chat"]);

  conversationStage = "CHOICE";
  return;
}

  // -------- CHOICE --------
 if (conversationStage === "CHOICE") {
  const normalized = input.toLowerCase().trim();
  
  if (normalized.includes("tip")) {
    cachedTips.forEach(tip => {
      typeBotMessage("• " + tip);
    });

    showOptions(["View support links", "Restart chat"]);
    return;
  }

  if (normalized.includes("support")) {

    cachedLinks.forEach(link => {
      addLinkMessage(link);
  });

    showOptions(["View tips", "Restart chat"]);
    return;
}

  if (normalized.includes("restart")) {
    conversationStage = "EMOTION";
    typeBotMessage("Let’s start again 😊 How are you feeling?");
    showOptions(["stressed", "anxious", "sad", "tired"]);
    return;
  }
}
}

// ---------------- DOM LOAD ----------------

document.addEventListener("DOMContentLoaded", () => {

  messages = document.getElementById("messages");
  inputEl = document.getElementById("userInput");
  sendBtn = document.getElementById("sendBtn");

  // initial bot message
typeBotMessage(
  "DISCLAIMER:" +
  " This Chatbot provides general wellbeing information only and does not replace professional medical or mental health support. " +
  "If you are in immediate danger, please contact emergancy services or NHS 111"
);


  addMessage(
    "Bot",
    "Hi 👋 Type one word that best describes how you're feeling, or choose an option below."
  );
  showOptions(["stressed", "anxious", "sad", "tired"]);

  // SEND BUTTON
  sendBtn.addEventListener("click", () => {
    const text = inputEl.value.trim();
    inputEl.value = "";
    if (!text) return;
    handleUserInput(text);
  });

  // ENTER KEY
  inputEl.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      const text = inputEl.value.trim();
      inputEl.value = "";
      if (!text) return;
      handleUserInput(text);
    }
  });


// SIDE BAR LOGIC
const sidebar = document.getElementById("sidebar");
const toggle = document.getElementById("toggleSidebar");

toggle.addEventListener("click", () => {
  sidebar.classList.toggle("collapsed");
});


});

loadMoodHistory();



