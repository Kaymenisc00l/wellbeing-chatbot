function sendMessage(){
    const input = document.getElementById('userInput').ariaValueMax;

    const chatArea = document.getElementById('chat-area');
    chatArea.innerHTML += `<p><strong>You:</strong> ${input}</p>`;

    document.getElementById("messages")

}