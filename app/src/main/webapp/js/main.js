document.getElementById('username').innerHTML = "Yuval";
var log = document.getElementById('log');

function onExperienceCreated(savedState) {
  log.innerHTML += Date.now() + ": onExperienceCreated() with state - " + savedState + "<br/>";
}

function saveExperienceState() {
  log.innerHTML += Date.now() + ": saveExperienceState()<br/>";
  return log.innerHTML;
}

function onExperiencePaused() {
  log.innerHTML += Date.now() + ": onExperiencePaused()<br/>";
}

function onExperienceResumed() {
  log.innerHTML += Date.now() + ": onExperienceResumed()<br/>";
}