library ieee;
use ieee.std_logic_1164.all;

entity GarageDoorController_4thversion is
port(
		reset 		: in std_logic;
		clk			: in std_logic;
		B				: in std_logic;
		Sopen			: in std_logic;
		Sclose		: in std_logic;
		Spresence	: in std_logic;
		ONoff			: out std_logic;
		OPENclose	: out std_logic
);
end GarageDoorController_4thversion;

architecture behavioral of GarageDoorController_4thversion is

type STATE_TYPE is (STATE_CLOSE, STATE_OPENING, STATE_OPEN, STATE_CLOSING);

signal CurrentState, NextState : STATE_TYPE;

begin

-- Flip-Flop's 
CurrentState <= STATE_CLOSE when RESET = '1' else NextState when rising_edge(clk);

-- Generate Next State 
GenerateNextState:
process (CurrentState, B, Sopen, Sclose, Spresence)
	begin
		case CurrentState is
			when STATE_CLOSE		=>	if (B = '1') then 
												NextState <= STATE_OPENING;
											else 
												NextState <= STATE_CLOSE;
											end if;
											
			when STATE_OPENING	=>	if (Sopen = '1') then 
												NextState <= STATE_OPEN;
											else 
												NextState <= STATE_OPENING;
											end if;
											
			when STATE_OPEN		=>	if (B = '1') then 
												NextState <= STATE_CLOSING;
											else 
												NextState <= STATE_OPEN;
											end if;
											
			when STATE_CLOSING	=>	if (Spresence ='1') then 
												NextState <= STATE_OPENING;
											elsif (Spresence ='0' and Sclose = '1') then
												NextState <= STATE_CLOSE;
											else 
												NextState <= STATE_CLOSING;
											end if;
		end case;
	end process;

-- Generate outputs
ONoff <= '1' when ( (CurrentState = STATE_OPENING and Sopen ='0') 
					or (CurrentState = STATE_CLOSING and Spresence ='0' and Sclose = '0')) 
					else '0';
					
OPENclose <= '1' when (CurrentState = STATE_OPENING) else '0';

end behavioral;