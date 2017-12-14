var control = $(".control");
// start = $(".start")

var currentLeft,
    currentTop,
    startClientX,
    startClientY,
    endClientX,
    endClientY,
    maxX,
    maxY,
    nowX,
    nowY;
var basicTop = -148,
    basicLeft = -117;


// console.log(control);

// control[0].addEventListener("touchstart", touchstart);
// control[0].addEventListener("touchmove", touchmove);

// start.addEventListener("click", start);


// function start() {
control[0].addEventListener("touchstart", touchstart);
control[0].addEventListener("touchmove", touchmove);
// }


//获取手指点击起始位置
function touchstart(event) {
    // console.log(currentLeft);
    startClientX = event.touches[0].clientX;
    startClientY = event.touches[0].clientY;
    currentLeft = parseFloat(control.css("left"));
    currentTop = parseFloat(control.css("top"))
}

//按住手指拖动目标
function touchmove(event) {
    endClientX = event.touches[0].clientX;
    endClientY = event.touches[0].clientY;
    // console.log(endClientX);
    nowX = currentLeft + endClientX - startClientX;
    nowY = currentTop + endClientY - startClientY;
    maxX = innerWidth - parseInt(control.css("width"));
    maxY = innerHeight - parseInt(control.css("height"));

    if (nowX < 0) {
        nowX = 0;
    } else if (nowX > maxX) {
        nowX = maxX
    }
    if (nowY < 0) {
        nowY = 0;
    } else if (nowY > maxY) {
        nowY = maxY;
    }

    control.css("left", nowX);
    control.css("top", nowY);
}

//生成随机数
function getRandom(n, m) {
    var c = m - n + 1;
    return Math.floor(Math.random() * c + n);
}

//显示小偷
function showThief(num, dire) {
    var str = "<div class='thief" + dire + "' ><img src='../static/images/thief.png'></div>";
    
    var nowTop,
        nowLeft;
    

    for (var i = 0; i < num; i++) {
        // $("body").append(str);
        $(".indexbg").append(str);
    }



    $(".thief" + dire).each(function(index) {
    
        var perWidth = 117;

        var perHeight = 148;
        var n = innerWidth / perWidth;
        var m = innerHeight / perHeight;
        console.log(innerWidth);
        console.log(perWidth);
        console.log(n);




        switch (dire) {
            case "top":
                thiefMove(basicTop, "top");
                console.log(n);
                randomIndex = getRandom(0, n - 2);
                console.log(randomIndex);
                nowLeft = perWidth * randomIndex;
                console.log(nowLeft);
                $(this).css("left", nowLeft);
                break;
            case "left":
                thiefMove(basicLeft, "left");
                randomIndex = getRandom(0, m - 2);
                nowTop = perHeight * randomIndex;
                $(this).css("top", nowTop);
                break;
            case "right":
                thiefMove(basicLeft, "left");
                randomIndex = getRandom(0,m - 2);
                nowTop = perHeight * randomIndex;
                $(this).css("top", nowTop);
                break;
        }



    })

} 

function thiefMove(num, dire){
    setInterval(function(){
        num ++;
        $(".thief" + dire).each(function(index) {
            $(this).css(dire, num);
        });
    },10)
}
 

function over(){
    $(".over").css("display", "block");
    $(".indexbg").css({
        backgroundColor : "rgba(0,0,0,0.7)",
        zIndex: "100"
    });
}

// over();

// showThief(3, "left");


showThief(5, "top");
showThief(4, "left");

// setInterval(showThief(5, "marginTop"), 5000);

function showTime() {
    var second = 0;
    setInterval(function() {
        second ++;
    }, 1000);
}



// showTime();
// 
// 

// if()