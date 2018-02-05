var javaTest = Java.type('cards.web.TestJs');
var result = "error";

function onMessage(str) {
  result = str;
  return null; 
}

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

function test1() {
	javaTest.onWebMessage(onMessage);
	javaTest.connectWeb("127.0.0.1");
	javaTest.sendWebMessage("hello!");
	while(connectedWeb()) {
	   sleep(100);
	}
	return result;
}
