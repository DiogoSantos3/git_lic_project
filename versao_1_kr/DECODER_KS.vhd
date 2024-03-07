LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity DECODER_KS is
port(
	S_dec: in STD_LOGIC_VECTOR(1 downto 0);
	
	D_dec: out STD_LOGIC_VECTOR(3 downto 0)
	);
	
end DECODER_KS;

architecture structural of DECODER_KS is
begin

D_dec(0) = not (NOT (S_dec(0)) and not (S_dec(1)))

D_dec(1) = not (NOT (S_dec(0)) and S_dec(1))

D_dec(2) = not (S_dec(0) and not (S_dec(1)))


end structural;