angular.module('SteroidsApplication', [
  'supersonic'
])
    .controller('IndexController', function($scope, supersonic, GameService) {
            supersonic.ui.screen.setAllowedRotations(["landscapeLeft", "landscapeRight"]);
            GameService.initializePlayer();
            $scope.selected = null;
            $scope.buttonDown = function(buttonNum) {
                supersonic.logger.log('push');
                GameService.sendButton(buttonNum, true).error(function(data, status, headers, config) {
                    supersonic.logger.log('ERROR '+status+' '+data+' '+headers+' '+config);
                });
                var num = null;
                if (buttonNum=='arrowUp') num = 1;
                if (buttonNum=='arrowLeft') num = 2;
                if (buttonNum=='arrowRight') num = 3;
                if (buttonNum=='arrowDown') num = 4;
                if (buttonNum=='a') num = 5;
                if (buttonNum=='b') num = 6;
                $scope.selected = num;
            };
            $scope.buttonUp = function(buttonNum) {
                GameService.sendButton(buttonNum, false).error(function(data, status, headers, config) {
                    supersonic.logger.log('ERROR '+status+' '+data+' '+headers+' '+config);
                });
                $scope.selected = null;
            };
    })
    .factory('GameService', function($http) {
        var factory = {};
        var playerId = null;
        var serverIP = '10.0.0.100';
        var serverPort = '3000';
        factory.initializePlayer = function() {
            playerId = Math.floor((Math.random() * 99999));
        };
        factory.sendButton = function(buttonNum, status) {
            supersonic.logger.log('sent');
            return $http.post('http://'+serverIP+':'+serverPort+'/button', {button: buttonNum, status: status, playerId: playerId});
        };
        return factory;
    })
    .directive('ngTouchstart', [function() {
        return function(scope, element, attr) {

            element.on('touchstart', function(event) {
                scope.$apply(function() {
                    scope.$eval(attr.ngTouchstart);
                });
            });
        };
    }])
    .directive('ngTouchend', [function() {
        return function(scope, element, attr) {

            element.on('touchend', function(event) {
                scope.$apply(function() {
                    scope.$eval(attr.ngTouchend);
                });
            });
        };
    }]);