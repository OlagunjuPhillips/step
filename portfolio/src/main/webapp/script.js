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

function loadTasks() {
  const parameter = document.getElementById("parameter").value;
  console.log(document.getElementById("parameter").value);
  fetch("/data?parameterValue=" + parameter).then(response => response.json()).then((tasks) => {
    const taskListElement = document.getElementById("task-list");
    tasks.forEach((task) => {
      const linebreak = document.createElement("br");
      taskListElement.appendChild(createTaskElement(task));
      taskListElement.appendChild(linebreak);
    })
  });
}

/** Creates an element that represents a task, including its delete button. */
function createTaskElement(task) {
  const taskElement = document.createElement("li");
  taskElement.className = "task";

  const titleElement = document.createElement("span");
  titleElement.innerText = task.title;

  taskElement.appendChild(titleElement);
  return taskElement;
}