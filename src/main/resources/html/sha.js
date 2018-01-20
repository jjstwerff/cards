/**
 *  Secure Hash Algorithm (SHA1)
 *  http://www.webtoolkit.info/
 *  Beware that this is not a good hash function to identify sensitive information.
 **/

function sha1(msg) {
  function rotate_left(n, s) {
    var t4 = (n << s) | (n >>> (32 - s));
    return t4;
  };

  function int32(x) {
    return x & 0x0ffffffff;
  };

  function bytes(val, arr, p) {
    for (var i = 3; i >= 0; i--)
      arr[p++] = (val >>> (i * 8)) & 0xff;
    return p;
  };

  var chunk;
  var i, A, B, C, D, E, F, K, temp;
  var W = new Uint32Array(80);

  // Initialize variables:
  var H0 = 0x67452301;
  var H1 = 0xEFCDAB89;
  var H2 = 0x98BADCFE;
  var H3 = 0x10325476;
  var H4 = 0xC3D2E1F0;

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
    A = H0;
    B = H1;
    C = H2;
    D = H3;
    E = H4;

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
    H0 = int32(H0 + A);
    H1 = int32(H1 + B);
    H2 = int32(H2 + C);
    H3 = int32(H3 + D);
    H4 = int32(H4 + E);
  }

  // Produce the final hash value (big-endian) as a 160-bit number:
  var buf = new Uint8Array(20);
  var p = bytes(H0, buf, 0);
  p = bytes(H1, buf, p);
  p = bytes(H2, buf, p);
  p = bytes(H3, buf, p);
  bytes(H4, buf, p);
  return String.fromCharCode.apply(null, buf);
}
