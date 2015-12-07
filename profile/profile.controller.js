(function () {
    'use strict';

    angular
        .module('app')
        .controller('ProfileViewController', ProfileViewController);

    ProfileViewController.$inject = ['UserService', '$location','$rootScope', '$routeParams', 'FlashService'];
    function ProfileViewController(UserService, $location, $rootScope, $routeParams, FlashService) {
        var vm = this;
        vm.profileUser = null;
        vm.hasRequest = false;
        vm.isFriend = false;
        vm.alreadyRequested = false;
        vm.profileUsername = $routeParams.profileUsername;
        vm.user = $rootScope.globals.currentUser;
        vm.requestedSent = false;

        vm.sendRequest = sendRequest;
        vm.acceptRequest = acceptRequest;
        vm.rejectRequest = rejectRequest;
        initController();

        function initController() {
            getProfileUser();
            updateStatus();
        }
		
        function getProfileUser() {
            UserService.GetByUsername(vm.profileUsername)
            .then(function (response) {
                vm.profileUser = response.data;
            });
        }
		
        function updateStatus() {
            UserService.GetStatus(vm.user.username,vm.profileUsername)
                .then(function (response) {
                    vm.hasRequest = response.data.hasRequest;
                    vm.isFriend = response.data.isFriend;
                    vm.alreadyRequested = response.data.alreadyRequested;
                });
        }

        function sendRequest() {
            vm.alreadyRequested = true;
            vm.requestedSent = true;
            UserService.SendRequest(vm.user.username,vm.profileUsername)
                .then(function (response) {
                    if (response.data.success) {
                        FlashService.Success('Request Sent successfully', true);
                    } else {
                        FlashService.Error(response.message);
                    }
                });
        }
		
        function acceptRequest() {
            vm.isFriend = true;
            UserService.Accept(vm.user.username,vm.profileUsername)
                .then(function (response) {
                    if (response.data.success) {
                        FlashService.Success('Request Accpetd successfully', true);
                    } else {
                        FlashService.Error(response.message);
                        vm.isFriend = false;
                    }
                });
        }
		
        function rejectRequest() {
            vm.isFriend = false;
            UserService.Reject(vm.user.username,vm.profileUser.username)
                .then(function (response) {
                    if (response.data.success) {
                        FlashService.Success('Request Rejected', true);
                    } else {
                        FlashService.Error(response.message);
                    }
                });
        }
    }

})();
