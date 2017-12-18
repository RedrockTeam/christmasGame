function getRank() {
    var req = packReq('GET-RANK', 'POST', null, null)
    request(req, function(data) {
        var myimg = data.data.my.imgurl,
            mynickname = data.data.my.nickname,
            myrank = data.data.my.rank;
        $(".myimg").html('<img src=' + myimg + '>');
        $(".nickname-info").html(mynickname);
        $(".place").html(myrank);
        console.log(data.data);

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