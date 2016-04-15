


var myBlockList = [];
var myWhiteList = [];
var serverBlockList = '';

$(function () {
	
	console.log( "start..." );
		

	// prende myblock
	var s = AndroidFunction.sharedGet( "myblock" );
	if( s == "" ) 
		myBlockList = [];
	else
		myBlockList = JSON.parse( s );
		
	// verifica whitelist
	var s = AndroidFunction.sharedGet( "mywhite" );
	if( s == "" ) 
		myWhiteList = [];
	else
		myWhiteist = JSON.parse( s );
	
			
	
	//var myTimer = setInterval(function(){ console.log( "time..." ); }, 1000);
	// chiudi lista
	$('body').on('click tap touchstart', '.toolCall', function () {
		$(".tool").removeClass( "toolSel" );
		$(".toolCall").addClass( "toolSel" );

		App.load("home");
	});

	$('body').on('click tap touchstart', '.toolBlock', function () {
		$(".tool").removeClass( "toolSel" );
		$(".toolBlock").addClass( "toolSel" );		

		App.load("block");
	});

	$('body').on('click tap touchstart', '.toolMyBlock', function () {
		$(".tool").removeClass( "toolSel" );
		$(".toolMyBlock").addClass( "toolSel" );

		App.load("myblock");
	});

	$('body').on('click tap touchstart', '.toolSettings', function () {
		$(".tool").removeClass( "toolSel" );
		$(".toolSettings").addClass( "toolSel" );

		App.load("settings");
	});
	
	
	
	
});




function manage(num,bb) {
	
	num = num.substr(1);

	// mette in blocco
	if( $("#p"+num).hasClass("block_point_1") == false ) {
		if( $.inArray(num, myBlockList) <= 0 ) {
			myBlockList.push( "+" + num );
		}
		
		if( $.inArray(num, myWhiteList) <= 0 ) {
			myWhiteList.push( "+" + num );
		}
		
	} else {
		// sblocca
		myBlockList.splice($.inArray("+" + num, myBlockList),1);
		
		// se non è già in whitelist e il numero è tra i server
		if( $.inArray(num, myWhiteList) <= 0 ) {
			var npos = jsonCall.strBlackList.indexOf( num );
			if( npos >= 0 && npos <= jsonCall.strBlackList.indexOf( "|" ) ) {
				myWhiteList.push( "+" + num );
				AndroidFunction.sharedPut( "mywhite", JSON.stringify( myWhiteList ) );
			}
		}		
	}
	
	AndroidFunction.sharedPut( "myblock", JSON.stringify( myBlockList ) );
	
	
	if( $("#p"+num).hasClass("block_point_1") || $("#mb"+num).hasClass("block_point_1") ) {
		$("#p"+num).removeClass("block_point_1");
		$("#mb"+num).removeClass("block_point_1");
		$("#mw"+num).removeClass("block_point_1");
		$(".p"+num).removeClass("block_point_1").addClass("block_point_0");
		$(".b"+num).removeClass("block_1").addClass("block_0");
		$(".d"+num).removeClass("block_back");
		$(".t"+num).html("block");
	} else {
		$("#p"+num).addClass("block_point_1");
		$("#mb"+num).addClass("block_point_1");
		$("#mw"+num).removeClass("block_point_1");

		$(".p"+num).removeClass("block_point_0").addClass("block_point_1");
		$(".b"+num).removeClass("block_0").addClass("block_1");
		$(".d"+num).addClass("block_back");
		$(".t"+num).html("accept");
		
	}
	

	
	jsonCall.strBlackList = serverBlockList + "|" + myBlockList.join(',');
	AndroidFunction.sharedPut( "blacklist", jsonCall.strBlackList );
	AndroidFunction.sharedPut( "myblock", JSON.stringify( myBlockList ) );

	
		
}


// ms to time/duration
msToDuration = function(ms){
	
    var seconds = ms / 1000;
    var hh = Math.floor(seconds / 3600),
    mm = Math.floor(seconds / 60) % 60,
    ss = Math.floor(seconds) % 60,
    mss = ms % 1000;
    //return pad(hh,2)+':'+pad(mm,2)+':'+pad(ss,2)+'.'+pad(mss,3);
    //mss = ms % 1000;
    return pad(hh,2)+':'+pad(mm,2)+':'+pad(ss,2);

}


function radians(n) {
  return n * (Math.PI / 180);
}
function degrees(n) {
  return n * (180 / Math.PI);
}

function getBearing(startLat,startLong,endLat,endLong){
  startLat = radians(startLat);
  startLong = radians(startLong);
  endLat = radians(endLat);
  endLong = radians(endLong);

  var dLong = endLong - startLong;

  var dPhi = Math.log(Math.tan(endLat/2.0+Math.PI/4.0)/Math.tan(startLat/2.0+Math.PI/4.0));
  if (Math.abs(dLong) > Math.PI){
    if (dLong > 0.0)
       dLong = -(2.0 * Math.PI - dLong);
    else
       dLong = (2.0 * Math.PI + dLong);
  }

  return (degrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;
}

function distanceRound( n ) {
	return Math.round(n / 10) * 10;
}

function distanceCalc(lat1, lon1, lat2, lon2, unit) {
	

	var radlat1 = Math.PI * lat1/180
	var radlat2 = Math.PI * lat2/180
	var radlon1 = Math.PI * lon1/180
	var radlon2 = Math.PI * lon2/180
	var theta = lon1-lon2
	var radtheta = Math.PI * theta/180
	var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
	
	
	dist = Math.acos(dist)
	dist = dist * 180/Math.PI
	dist = dist * 60 * 1.1515
	if (unit=="M") { dist = dist * 1.609344 * 1000 }
	if (unit=="K") { dist = dist * 1.609344 }
	if (unit=="N") { dist = dist * 0.8684 }
	return dist
}  


function convertUTCDateToLocalDate(date) {
	offset = date.getTimezoneOffset()*60*1000;
	offset *= -1;
	
    var newDate = new Date(date.getTime()+offset);

    var offset = date.getTimezoneOffset() / 60;
    var hours = date.getHours();

    newDate.setHours(hours - offset);

    return newDate;   
}




function findIndexByKeyValue(obj, key, value)
{
    for (var i = 0; i < obj.length; i++) {
        if (obj[i][key] == value) {
            return i;
        }
    }
    return null;
}

function getByIndex(obj,index)
{
    for (var i = 0; i < obj.length; i++) {
        if( i == index ) {
            return obj[i];
        }
    }
    return null;
}

// To have leading zero digits in strings.
function pad(num, size) {
    var s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
}



