function sendMessage() {
  const inputEl = document.getElementById("userInput");
  const text = inputEl.value.trim();
  if (!text) return;

  const messages = document.getElementById("messages");

  messages.innerHTML += `<p><strong>You:</strong> ${text}</p>`;

  inputEl.value = "";

  messages.scrollTop = messages.scrollHeight;
}

document.addEventListener("DOMContentLoaded", () => {
  const sendBtn = document.getElementById("sendBtn");
  const inputEl = document.getElementById("userInput");

  sendBtn.addEventListener("click", sendMessage);


  inputEl.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      sendMessage();
    }
  });
});