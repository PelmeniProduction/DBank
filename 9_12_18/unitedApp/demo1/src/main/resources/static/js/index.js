var UPDATE_BUTTON = 'update_data';//id кнопки
var RADIOBUTTON_CLASS = '.custom-control-input';//класс радиобатонов
var SELECT_CLASS = '.form-control';//класс выпадающего окна

function init() {
    var Update = document.getElementById(UPDATE_BUTTON);//находим кнопку по id
    Update.addEventListener("click", function (e) {//обрабатываем клик на неё
        getData();
        //document.location.href = 'http://localhost:8080/update';
    });
}
function getData() {
    console.log('function called');
    var Select = document.querySelector(SELECT_CLASS);
    console.log(Select.options[Select.selectedIndex].text);//выводим выбранный пункт выпадающего меню select
    var RadioButton = document.querySelectorAll(RADIOBUTTON_CLASS);
    for (var i = 0; i < RadioButton.length; i++) {
        if (RadioButton[i].type === "radio" && RadioButton[i].checked) {
            console.log(RadioButton[i].id);//выводим выбранный пункт radiobutton
            //ajax-запрос-post
            $.ajax({
                type: "POST",
                url: "/update",
                data: JSON.stringify({"category": Select.options[Select.selectedIndex].text, "period": RadioButton[i].id}),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data){alert(data);},
                failure: function (errMsg) {alert(errMsg);}
            })
            //post-запрос на jquery
            /*$.post("/update", {category: Select.options[Select.selectedIndex].text, period: RadioButton[i].id})
                .done(function (data) {
                    alert("DataLoaded:" + data);
                });*/
            //post запрос на XMLHttpRequest
            /*var xhr = new XMLHttpRequest();
            xhr.open("POST", '/update', true);
            xhr.send({
                category: Select.options[Select.selectedIndex].text,
                period: RadioButton[i].id
            });
            xhr.onreadystatechange = function (e) {
                if (this.readyState !== 4) return;
                if (this.status === 200) {
                    console.log("OK");
                }
            };*/
        }
    }
}
document.addEventListener('DOMContentLoaded', init);//Ждём пока страница загрузится, затем вызываем функцию init