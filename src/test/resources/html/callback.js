var javaTest = Java.type('cards.web.TestJs');

function called(str) {
  return str == 'Hi' ? "Bye" : null; 
}

function test1() {
	return javaTest.toCall(called) == 1 ? "Cool" : null;
}
