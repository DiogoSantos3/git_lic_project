LIBRARY IEEE;
use IEEE.std_logic_1164.all;

entity MUX_KS is

port(
	M_KS: in STD_LOGIC_VECTOR(3 downto 0);
	S_KS: in std_logic_vector(1 downto 0);
	Ym_KS: out std_logic
	);
END MUX_KS;

architecture structural OF MUX_KS is 

begin


Ym_KS <= (not S_KS(1) and not S_KS(0) and M_KS(0)) or (not S_KS(1) and S_KS(0) and M_KS(1)) or (S_KS(1) and not S_KS(0) and M_KS(2)) or (S_KS(1) and S_KS(0) and M_KS(3));


end structural;