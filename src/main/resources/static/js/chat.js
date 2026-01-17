alert("chat.js loaded");

let conversationStage = "EMOTION";

let messages;
let inputEl;
let sendBtn;

// ---------------- UI ----------------

function addMessage(sender, text) {
  messages.innerHTML += `<p><strong>${sender}:</strong> ${text}</p>`;
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

// ---------------- CHAT LOGIC ----------------

async function handleUserInput(input) {

  addMessage("You", input);

  // -------- EMOTION --------
  if (conversationStage === "EMOTION") {

    const res = await fetch("/api/chat/feeling", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ feeling: input })
    });

    const data = await res.json();

    addMessage("Bot", data.message);
    showOptions(data.nextOptions);

    conversationStage = "CATEGORY";
    return;
  }

  // -------- CATEGORY --------
  if (conversationStage === "CATEGORY") {

    if (input.toLowerCase().includes("restart")) {
      conversationStage = "EMOTION";
      addMessage("Bot", "Let’s start again 😊 How are you feeling?");
      showOptions(["stressed", "anxious", "sad", "tired"]);
      return;
    }

    const res = await fetch("/api/chat/category", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ category: input })
    });

    const data = await res.json();

    addMessage("Bot", data.message);
    showOptions(data.nextOptions);

    conversationStage = "GUIDANCE";
    return;
  }

  // -------- GUIDANCE --------
  if (conversationStage === "GUIDANCE") {

    if (input.toLowerCase().includes("restart")) {
      conversationStage = "EMOTION";
      addMessage("Bot", "Let’s start again 😊 How are you feeling?");
      showOptions(["stressed", "anxious", "sad", "tired"]);
      return;
    }

    addMessage("Bot", "If you'd like to restart, type or click 'Restart chat'.");
  }
}

// ---------------- DOM LOAD ----------------

document.addEventListener("DOMContentLoaded", () => {

  messages = document.getElementById("messages");
  inputEl = document.getElementById("userInput");
  sendBtn = document.getElementById("sendBtn");

  // initial bot message
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

});
