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

function getDataPage() {
    const dataPromise = fetch("/data")

    dataPromise.then(handleData);
}

function handleData(data) {
  const dataPromise = data.text();


  dataPromise.then(addDataToDom);
}

function addDataToDom(data) {
  const dataContainer = document.getElementById('data-container');
  
  dataContainer.innerText = commentsList(splitComments(data));
  console.log();
}

function splitComments(data) {
    var comments = data.split(",");
    return comments;
}

function commentsList(data) {
    var comments = "";
    for (i = 0; i < data.length; i++) {
        comments += data[i] + "\n";
        console.log(data[i]);
    }
    return comments;
}
