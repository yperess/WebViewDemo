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
  var user = JSON.parse(AppUser.loadSelf());
  log.innerHTML += Date.now() + ": username? " + user.username + ", icon_url? " + user.icon_url + "<br/>";
  App.User.loadSelf().then((asyncUser) => {
    log.innerHTML += Date.now() + ": asyncUser? " + asyncUser + "<br/>";
  });
  log.innerHTML += Date.now() + ": started async App.User.loadSelf()<br/>";
}