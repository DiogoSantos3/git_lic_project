library ieee;
use ieee.std_logic_1164.all;

entity Mux_kr_tb is
end Mux_kr_tb;

architecture behavioral of Mux_kr_tb is

component Mux_kr is
port(
	M_kr: in STD_LOGIC_VECTOR(3 downto 0);
	S_kr: in std_logic_vector(1 downto 0);
	Ym_kr: out std_logic
	);
	
end component;

-- UUT signals
constant MCLK_PERIOD : time := 20 ns;
constant MCLK_HALF_PERIOD : time := MCLK_PERIOD / 2;


signal 	M_kr_tb: in STD_LOGIC_VECTOR(3 downto 0);
signal	S_kr_tb: in std_logic_vector(1 downto 0);
signal	Ym_kr_tb: out std_logic;

begin

-- Unit Under Test

U1: Mux_kr
	port map(M_kr => M_kr_tb,
	S_kr => S_kr_tb,
	Ym_kr => Ym_kr_tb
	);

	
clk_gen : process
begin
		clk_tb <= '0';
		wait for MCLK_HALF_PERIOD;
		clk_tb <= '1';
		wait for MCLK_HALF_PERIOD;
end process;

stimulus: process 
begin
