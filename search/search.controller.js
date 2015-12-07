(function () {
    'use strict';

    angular
        .module('app')
        .controller('SearchController', SearchController);

    SearchController.$inject = ['UserService', '$rootScope'];
    function SearchController(UserService, $rootScope) {
        var vm = this;
        vm.allUsers = [];
        
        initController();

        function initController() {
            loadAllUsers();
        }
        
        function loadAllUsers() {
            UserService.GetAll($rootScope.globals.currentUser.username)
                .then(function (response) {
                    vm.allUsers = response.data.allUsers;
                });
        }
    }

})();