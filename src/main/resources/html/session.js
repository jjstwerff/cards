var ws = null;

class SomeThing {
}

if (typeof WebSocket === "undefined")
  load('src/main/resources/html/websocket.js');

function session(secret, name) { 
  var ws = new WebSocket("ws://localhost:8080");
  ws.onopen = function(event) {
    exampleSocket.send(JSON.stringify({click:{type:"wall",nr:2,x:12,y:424}}));
  };
  ws.onmessage = function(event) {
    var msg = JSON.parse(event.data);
  };
  ws.onclose = function() {
    ws = null;
  };
}

/**
 *  Secure Hash Algorithm (SHA1)
 *  http://www.webtoolkit.info/
 *  Beware that this is not a good hash function as unique identification of sensitive information.
 **/

function sha1(msg) {
  function rotate_left(n, s) {
    var t4 = (n << s) | (n >>> (32 - s));
    return t4;
  };

  function int32(x) {
    return x & 0x0ffffffff;
  };

  var chunk, i, A, B, C, D, E, F, K, temp;
  var W = new Uint32Array(80);
  var buffer = new ArrayBuffer(20);
  var H = new Uint32Array(buffer);

  // Initialize variables:
  H[0] = 0x67452301;
  H[1] = 0xEFCDAB89;
  H[2] = 0x98BADCFE;
  H[3] = 0x10325476;
  H[4] = 0xC3D2E1F0;

  // Pre-processing:
  var msg_len = msg.length;
  var word_array = new Array();
  for (i = 0; i < msg_len - 3; i += 4)
    word_array.push(msg.charCodeAt(i) << 24 | msg.charCodeAt(i + 1) << 16 | msg.charCodeAt(i + 2) << 8 |
      msg.charCodeAt(i + 3))
  if (msg_len % 4 == 0)
    word_array.push(0x080000000);
  else if (msg_len % 4 == 1)
    word_array.push(msg.charCodeAt(msg_len - 1) << 24 | 0x0800000);
  else if (msg_len % 4 == 2)
    word_array.push(msg.charCodeAt(msg_len - 2) << 24 | msg.charCodeAt(msg_len - 1) << 16 | 0x08000);
  else
    word_array.push(msg.charCodeAt(msg_len - 3) << 24 | msg.charCodeAt(msg_len - 2) << 16 |
        msg.charCodeAt(msg_len - 1) << 8 | 0x80);
  while ((word_array.length % 16) != 14) word_array.push(0);
  word_array.push(msg_len >>> 29);
  word_array.push(int32(msg_len << 3));

  // Process the message in successive 512-bit chunks:
  for (chunk = 0; chunk < word_array.length; chunk += 16) {
    for (i = 0; i < 16; i++)
      W[i] = word_array[chunk + i];

    // Extend the sixteen 32-bit words into eighty 32-bit words:
    for (i = 16; i <= 79; i++)
      W[i] = rotate_left(W[i - 3] ^ W[i - 8] ^ W[i - 14] ^ W[i - 16], 1);

    // Initialize hash value for this chunk:
    A = H[0];
    B = H[1];
    C = H[2];
    D = H[3];
    E = H[4];

    // Main loop:
    for (i = 0; i <= 79; i++) {
      if (i <= 19) {
	    F = (B & C) | (~B & D);
	    K = 0x5A827999;
      } else if (i <= 39) {
	    F = B ^ C ^ D;
	    K = 0x6ED9EBA1;
	  } else if (i <= 59) {
	    F = (B & C) | (B & D) | (C & D);
	    K = 0x8F1BBCDC;
	  } else {
	    F = B ^ C ^ D;
	    K = 0xCA62C1D6;
	  }
	  temp = rotate_left(A, 5) + F + E + W[i];
      E = D;
      D = C;
      C = rotate_left(B, 30);
      B = A;
      A = int32(temp + K);
    }

    // Add this chunk's hash to result so far:
    H[0] = int32(H[0] + A);
    H[1] = int32(H[1] + B);
    H[2] = int32(H[2] + C);
    H[3] = int32(H[3] + D);
    H[4] = int32(H[4] + E);
  }
  return buffer;
}

function sha1str(msg) {
  var from = new Uint8Array(sha1(msg));
  var into = new Uint8Array(20);
  // Change byte order per word
  for (var i = 0; i < 20; i++)
    into[i] = from[4 * (i >> 2) + 3 - i % 4];
  return String.fromCharCode.apply(null, new Uint8Array(into));
}
