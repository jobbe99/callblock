var json = "https://www.callcenterblock.com/app/json.php";
var profile = {id:-1,nStatus:0};



// JavaScript Document
var getOS = {
	
	userOS: "",
  	userOSver:0,
	
  
  	get: function() {
		
	var ua = navigator.userAgent;
	var uaindex;
	
	// determine OS
	if ( ua.match(/iPad/i) || ua.match(/iPod/i) || ua.match(/iPhone/i) ) {
	this.userOS = 'iOS';
	uaindex = ua.indexOf( 'OS ' );
	}
	else if ( ua.match(/Android/i) ) {
	this.userOS = 'Android';
	uaindex = ua.indexOf( 'Android ' );
	} else {
	this.userOS = 'unknown';
	}
	
	// determine version
	if ( this.userOS === 'iOS'  &&  uaindex > -1 ) {
	this.userOSver = ua.substr( uaindex + 3, 3 ).replace( '_', '.' );
	} else if ( this.userOS === 'Android'  &&  uaindex > -1 ) {
	this.userOSver = ua.substr( uaindex + 8, 3 );
	} else {
	this.userOSver = 'unknown';
	}
	}
	
	
};



var popup = {
	div: '.divPop',
	divMsg: '.popup',
	
	show: function( msg, sec ) {
		this.div = ".divPop";
		$( this.divMsg ).html( msg );
		$( this.div ).css("display", "block").css("opacity", "1").unbind("transitionend webkitTransitionEnd oTransitionEnd otransitionend");
		if( sec > 0 )
			d = this.div;
			setTimeout( function() {
 			   hidden(d);
			}, sec * 1000); // after 1 sec
	},
	
	choice: function() {
		this.div = '.divChoice';
		$(this.div).css("display", "block").css("opacity", "1").unbind("transitionend webkitTransitionEnd oTransitionEnd otransitionend");
	},
	
	
	
}

function hidden(div) {
	$(div).css("opacity", "0").on('transitionend webkitTransitionEnd oTransitionEnd otransitionend', hide(div) );
}
	
function hide(div){
    $(div).css("display", "none");
}
	



function choice( msg, nSec ) {
	$(".divChoice").css("display", "block").css("opacity", "1").unbind("transitionend webkitTransitionEnd oTransitionEnd otransitionend");
	
	if( nSec > 0 )
		setTimeout("choice_hidden()", nSec * 1000); // after 1 sec
}

function choice_hidden(){
    $(".divChoice").css("opacity", "0").on('transitionend webkitTransitionEnd oTransitionEnd otransitionend', choice_hide);
}

function choice_hide(){
    $(".divChoice").css("display", "none");
}



/** FACEBOOK **/

/*
var fbLoginSuccess = function (userData) {
	alert( "success" );
	processFacebook();
}
*/


var fbLogoutSuccess = function() {
	var s = { 
		cmd: 'fb-logout',
		idutente: profile.id,
	};
	
	dataSend( s, function( res ) {		
		if( res.success == 1 ) 
			profile.nStatus = 0;	
	});
}


 
function fbInit() {	
	var fbLoginSuccess = function (userData) {
		alert("UserInfo: " + JSON.stringify(userData));
	}
	
	/*
		facebookConnectPlugin.login(["public_profile"],
			fbLoginSuccess,
			function (error) { alert("" + error) }
		);
	*/			
}

var fbLoginSuccess = function (userData) {
	processFacebook();

    /*
	alert("UserInfo: " + JSON.stringify(userData));
    facebookConnectPlugin.getAccessToken(function(token) {
        alert("Token: " + token);
		processFacebook();

    }, function(err) {
        alert("Could not get access token: " + err);
    });
	*/
}



function processFacebook()  {
		
	//alert("processFacebook");
	//facebookConnectPlugin.api("me/?fields=id,email", ["public_profile"], function (result) {
	facebookConnectPlugin.api('/me?fields=id,last_name,first_name', null, function(response) {
		console.log( response );
		alert("ret: " + JSON.stringify(response));
		fbSet( response.id, response.first_name, response.last_name );
	});
		

} 
		

function fbSet( id, first_name, last_name ) {
	var s = { 
		cmd: 'fbSet',
		idutente: profile.id,
		fb_id: id,
		fb_first_name: first_name,
		fb_last_name: last_name
	};
	
	dataSend( s, function( res ) {		
		if( res.success == 1 ) {
			profile.nStatus = 1;
		} else {
		}
	});
	
	
}

		
function fbc() {

	facebookConnectPlugin.api("<user-id>/?fields=id,email", ["user_birthday"],
		function (result) 
		{
			/* alerts:
				{
					"id": "000000123456789",
					"email": "myemail@example.com"
				}
			*/
			alert("Result: " + JSON.stringify(result));
		},
		function (error) 
		{
			alert("Failed: " + error);
		});

}



function profileGet() {
	
	profile = jQuery.parseJSON( window.localStorage.getItem("buca-profile") );
	

	if( profile == null || profile.id == -1 ) {
		profileCreate();
	} 
	
	
	// verifica il profilo
	getOS.get();
	
	
	/// RIMUOVEREEEEE
	if( getOS.userOS == 'unknown' ) {
		profile.id = 2;
	}
	
	
	var s = { cmd: 'profilo-get', id:profile.id, os:getOS.userOS, osver:getOS.userOSver };
	dataSend( s, function( res ) {		
		if( res.success == 1 ) {
			if( res.records > 0 )
				profile = res.data[0];
		} 
		
	});

	
}



function profileCreate() {

	var s = { 
		cmd: 'utente-new' 
	};
	
	//alert("create profile...");
	
	dataSend( s, function( res ) {
		if( res.success == 1 ) {
			console.log( JSON.stringify( res.data ) );			
			profile = JSON.stringify( res.data );

			window.localStorage.setItem( "buca-profile", profile );
			//var p = window.localStorage.getItem( "profile" );			
		}
	});
	
	
}



function jsGet( cmd, loadingFn, callback ) {

	if( window[loadingFn] )
		window[loadingFn](true); 

	var ret = {
		ok: 0
	};
	
	
	$.ajax({
		url: json + '?cmd=' + cmd,
		dataType: 'json',
		crossDomain: true,
		async: true,  
		success: function(data) {
			data.ok = 1;
			callback( data );
		},
		complete: function() {
			if( window[loadingFn] )
				window[loadingFn](false); 
		},
		error: function(xhr, status, error) {
			//alert( JSON.stringify(xhr)  + "-"+status+"-"+error);
			if( window[loadingFn] )
				window[loadingFn](false); 
			$(".divAlert").show();
			callback( ret );
			
		}
	}); // getjson
	
}	 



function dataSend( data, callback ) {

	var error = {
		success: 0
	};
	
	$.ajax({
		type: "POST",
		url: json,
		data: data,
		dataType: "json",
		success: function(res) {
			if( res.success == 1 ) {
				callback( res );
			} else
				callback( res );
		},
		complete: function() {
		},
	    error: function(error, x, y) {
			console.log( "errore" + JSON.stringify( error ) );
	    	callback( error );
		}
	});
	
}


function loading( b ) {
	if( b )
		$('.divLoading').show();
	else
		$('.divLoading').hide();
}




