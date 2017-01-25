var App = {
  User: {
    loadSelf: function() {
      return new Promise((resolve, reject) => {
        resolve(AppUser.loadSelf());
      });
    }
  }
}