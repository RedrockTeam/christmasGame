function getRank() {
    var req = packReq('GET-RANK', 'POST', null, null)
    request(req, function(data) {
        var myimg = data.data.my.imgurl,
            mynickname = data.data.my.nickname,
            myrank = data.data.my.rank;
        $(".myimg").html('<img src=' + myimg + '>');
        $(".nickname-info").html(mynickname);
        $(".place").html(myrank);
        console.log(data);
        console.log(data.data);
        var rankdata = data.data;

        for (var i = 1; i < 16; i++) {

            var rankimgurl = rankdata[i].imgurl,
                ranknickname = rankdata[i].nickname,
                rank = rankdata[i].rank;
            var placeItem = document.createElement("li"),
                placeImg = document.createElement("div"),
                placeNickname = document.createElement("div");
            $(placeItem).attr("class", "placeItem");
            $(placeImg).attr("class", "placeImg");
            $(placeNickname).attr("class", "placeNickname");
            $(".placeList").append(placeItem)
            placeItem.append(placeImg);
            placeItem.append(placeNickname);
            var img = document.createElement("img"),
                span = document.createElement("span");
            $(span).attr("class", "nicknameword")
            $(img).attr("src", rankimgurl);
            placeNickname.append(span);
            placeImg.append(img);
            $(span).html(ranknickname);
        }
    })
}



function postPoint(point) {
    var req = packReq('POST-POINT', 'POST', null, {
        score: parseInt(point * 100)
    });
    console.log(req.payload.score);
    request(req, function(data) {
        var rank = data.data.rank;
        $(".place").html(rank);
        console.log(rank);
    });
}