var UPDATE_BUTTON = 'update_data';//id кнопки
var RADIOBUTTON_CLASS = '.custom-control-input';//класс радиобатонов
var SELECT_CLASS = '.form-control';//класс выпадающего окна

function init(){
    var Update = document.getElementById(UPDATE_BUTTON);//находим кнопку по id
    //обрабатываем клик на неё
    Update.addEventListener("click", function (e){
        getData();
        //document.location.href = 'http://localhost:8080/update';
    });
}
function getData(){
    console.log('function called');
    //выводим выбранный пункт выпадающего меню select
    var Select = document.querySelector(SELECT_CLASS);
    console.log(Select.options[Select.selectedIndex].text);
    //выводим выбранный пункт radiobutton
    var RadioButton = document.querySelectorAll(RADIOBUTTON_CLASS);
    for(var i = 0; i<RadioButton.length; i++)
    {
        if (RadioButton[i].type === "radio" && RadioButton[i].checked)
        {
            console.log(RadioButton[i].id);
        }
    }
}

//Ждём пока страница загрузится, затем вызываем функцию init
document.addEventListener('DOMContentLoaded', init);