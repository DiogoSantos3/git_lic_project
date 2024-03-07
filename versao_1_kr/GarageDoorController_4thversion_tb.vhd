library ieee;
use ieee.std_logic_1164.all;

entity GarageDoorController_4thversion_tb is
end GarageDoorController_4thversion_tb;

architecture behavioral of GarageDoorController_4thversion_tb is

component GarageDoorController_4thversion is
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
end component;

-- UUT signals
constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;

signal reset_tb : std_logic;
signal clk_tb : std_logic;
signal B_tb : std_logic;
signal Sopen_tb : std_logic;
signal Sclose_tb : std_logic;
signal Spresence_tb : std_logic;
signal ONoff_tb : std_logic;
signal OPENclose_tb : std_logic;


begin

-- Unit Under Test
UUT: GarageDoorController_4thversion 
		port map(reset => reset_tb,
					clk	=> clk_tb,
					B 		=> B_tb,
					Sopen => Sopen_tb,
					Sclose => Sclose_tb,
					Spresence => Spresence_tb,
					ONoff	=> ONoff_tb,
					OPENclose => OPENclose_tb);

clk_gen : process
begin
		clk_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		clk_tb <= '1';
		wait for MCLK_HALF_PERIOD;
end process;

stimulus: process 
begin
	-- reset
	reset_tb <= '1';
	Sopen_tb <= '0';
	Sclose_tb <= '1';
	Spresence_tb <= '0';
	B_tb <= '0';
	wait for MCLK_PERIOD*2;
	
	reset_tb <= '0';
	wait for MCLK_PERIOD*2;

	B_tb <= '1';
	wait for MCLK_PERIOD*3;
	
	Sclose_tb <= '0';
	wait for MCLK_PERIOD*5;
	
	Sopen_tb <= '1';
	wait for MCLK_PERIOD*5;
	
	Spresence_tb <= '1';
	wait for MCLK_PERIOD*5;
	
	Spresence_tb <= '0';
	wait for MCLK_PERIOD*5;
	
	Sclose_tb <= '1';
	B_tb <=	'0';
	wait for MCLK_PERIOD*5;

	wait;
end process;

end architecture;