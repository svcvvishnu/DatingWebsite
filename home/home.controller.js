(function () {
    'use strict';

    angular
        .module('app')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['UserService', '$rootScope','FlashService'];
    function HomeController(UserService, $rootScope,FlashService) {
        var vm = this;
        vm.user = null;
        vm.connectedUsers = [];
        vm.acceptedUsers = [];
        vm.rejectedUsers = [];
        vm.pendingUsers = [];
        vm.doEdit = doEdit;
        vm.doUpdate = doUpdate;

        initController();

        function initController() {
            vm.showEdit = true;
            loadCurrentUser();
            loadRelatedUsers();
        }
        
        function doEdit(){
            FlashService.Success('In Edit Mode', true);
            vm.showEdit = false;
        }
        function doUpdate() {
            UserService.UpdateUser(vm.user)
                .then(function (response) {
                    FlashService.Success('Successfully Updated', true);
                    vm.showEdit = true;
                });
        }

        function loadCurrentUser() {
            UserService.GetByUsername($rootScope.globals.currentUser.username)
                .then(function (response) {
                    vm.user = response.data;
                });
        }

        function loadRelatedUsers() {
            UserService.GetRelatedUsers($rootScope.globals.currentUser.username)
                .then(function (response) {
                    vm.connectedUsers = response.data.connectedUsers;
                    vm.acceptedUsers = response.data.acceptedUsers;
                    vm.rejectedUsers = response.data.rejectedUsers;
                    vm.pendingUsers = response.data.pendingUsers;
                });
        }
    }

})();