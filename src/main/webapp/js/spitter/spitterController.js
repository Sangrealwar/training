/*
    将创建的SpitterServer注入到Controller中
 * Created by wq on 2017/3/16.
 */
angular.module('myApp').controller('spitterCtl', ['$scope', 'SpitterService', function($scope, SpitterService) {
    var self = this;
    self.spitter={id:null,username:'',password:'',firstName:'',lastName:''};
    self.spitters=[];

    self.submit = submit;
    self.edit = edit;
    self.remove = remove;
    self.reset = reset;

    fetchAllSpitters();

    function fetchAllSpitters(){
        SpitterService.fetchAllSpitters()
            .then(
                function(d) {
                    self.spitters = d.data;   //提取json对象中的data
                },
                function(errResponse){
                    console.error('Error while fetching Users');
                }
            );
    }

    function createSpitter(spitter){
        SpitterService.createSpitter(spitter)
            .then(
                fetchAllSpitters,
                function(errResponse){
                    console.error('Error while creating User');
                }
            );
    }

    function updateSpitter(spitter, id){
        SpitterService.updateSpitter(spitter, id)
            .then(
                fetchAllSpitters,
                function(errResponse){
                    console.error('Error while updating User');
                }
            );
    }

    function deleteSpitter(id){
        SpitterService.deleteSpitter(id)
            .then(
                fetchAllSpitters,
                function(errResponse){
                    console.error('Error while deleting User');
                }
            );
    }

    function submit() {
        if(self.spitter.id===null){
            console.log('Saving New User', self.spitter);
            createSpitter(self.spitter);
        }else{
            updateSpitter(self.spitter, self.spitter.id);
            console.log('User updated with id ', self.spitter.id);
        }
        reset();
    }

    function edit(id){
        console.log('id to be edited', id);
        for(var i = 0; i < self.spitters.length; i++){
            if(self.spitters[i].id === id) {
                self.spitter = angular.copy(self.spitters[i]);
                break;
            }
        }
    }

    function remove(id){
        console.log('id to be deleted', id);
        for(var i = 0; i < self.spitters.length; i++){
            if(self.spitters[i].id == id) {
                self.reset();
                break;
            }
        }
        deleteSpitter(id);
    }


    function reset(){
        self.spitter={id:null,username:'',password:'',firstName:'',lastName:''};
        $scope.myForm.$setPristine(); //reset Form
    }

}]);