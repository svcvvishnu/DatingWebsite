(function () {
    'use strict';

    angular
        .module('app')
        .controller('AdminController', AdminController);

    AdminController.$inject = ['UserService', '$rootScope'];
    function AdminController(UserService, $rootScope) {
        var vm = this;
        
        vm.allUsers = [];
        vm.passRequestUsers = [];
        
        loadAllUsers();
        loadPaswordRequestUsers();
      
        function loadAllUsers() {
            UserService.GetAll('Administrator')
                .then(function (response) {
                    vm.allUsers = response.data.allUsers;
                });
        }
        
        function loadPaswordRequestUsers() {
            UserService.GetPassReqUsers()
                .then(function (response) {
                    vm.passRequestUsers = response.data.allUsers;
                });
        }
    }
})();
