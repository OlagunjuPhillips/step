// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */



function favoriteShows() {
  const shows =
      ['Chuck', 'Lucifer', 'The Good Doctor', 'Love Death + Robots'];

  // Pick a random greeting.
  const show = shows[Math.floor(Math.random() * shows.length)];

  // Add it to the page.
  const showContainer = document.getElementById('show-container');
  showContainer.innerText = show;
}

function makebig(image) {
    image.style.height = "60px"
    image.style.width = "60px"
}

function normal(image) {
    image.style.height = "40px"
    iamge.style.width = "40px"
}

function loadComment() {
  const parameter = document.getElementById("parameter").value;
  

  fetch("/data?parameterValue="+ parameter).then(response => response.json()).then((comments) => {

    const commentListElement = document.getElementById("comment-list");
    commentListElement.innerHTML = "";
    
 
    comments.forEach((comment) => {  
      commentListElement.appendChild(createCommentElement(comment));
      commentListElement.appendChild(document.createElement("br"));
    })
  });
}


function createCommentElement(comment) {
  const linebreak = document.createElement("br");

  const commentElement = document.createElement("li");
  commentElement.className = "comment";

  const titleElement = document.createElement("span");
  titleElement.innerText = comment.email+": "+comment.title;

  const commentUpload = document.createElement("img");
  commentUpload.src = comment.url;
  commentUpload.width = 100;

  commentElement.appendChild(titleElement);
  commentElement.appendChild(linebreak);
  commentElement.appendChild(commentUpload);

  return commentElement;
}

function deleteComments(){
    fetch("/delete-data", {method:"POST"}).then(() => loadComment());
}

function addComments(){
    fetch("/user-login").then(response => response.text()).then((response) => {

        const commentBox = document.getElementById("commentForm");
        const loginLink = document.getElementById("login-link");
        const commentButton = document.getElementById("comment-button");


        if(response[0] == "1"){
            commentBox.style.display = "block";
            commentButton.style.display = "none";
        }
        else{
            loginLink.style.display = "block";
        }
    })
}

function fetchBlobstoreUrlAndShowForm() {
  fetch('/upload-url')
      .then((response) => {
        return response.text();
      })
      .then((imageUploadUrl) => {
        const messageForm = document.getElementById('commentForm');
        messageForm.action = imageUploadUrl;
        console.log("url recieved");
      });
}

function loadAllFunctions(){
    fetchBlobstoreUrlAndShowForm();
    loadComment();
}
