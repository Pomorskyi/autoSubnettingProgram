# Input

Input net: 192.168.40.240
Input mask (0 - 32): 24
Your network address: 192.168.40.0.
Input first letter for ordering(eg. A->Z: A): A
Input your subs(e.g (Z,118), (L,5), (B,1)): (Z,118), (L,5), (B,1)

# Output

NAME | PLACE | MASKdec | MASKbin - - - - - | NetworkAddress | Broadcast

Z - - - -| 128 - -| /25- - - - -| 255.255.255.128. | 192.168.40.0. - - | 192.168.40.127

L - - - -| 8 - - - -| /29- - - - -| 255.255.255.248. | 192.168.40.128. -| 192.168.40.135

B - - - -| 4 - - - -| /30- - - - -| 255.255.255.252. | 192.168.40.136. -| 192.168.40.139

SUM: 140
