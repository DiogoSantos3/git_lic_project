library ieee;
use ieee.std_logic_1164.all;

entity Counter_tb entidade is
end Counter_tb entidade ;

architecture Counter_tb e is

component Counter is
port(
	DataIn		: in std_logic_vector(3 downto 0);
	Clk_cont		: in std_logic;
	CE_cont		: in std_logic;
	RESET 		: in std_logic;
	PL 			: in std_logic;
	
	CQ: out STD_LOGIC_VECTOR(3 downto 0)
);
end component;	

-- UUT signals
constant TIME_TIME : time := 20 ns;
constant HALF_TIME : time := TIME_TIME / 2;

signal DataIn_0_tb : std_logic;
signal DataIn_1_tb : std_logic;
signal DataIn_2_tb : std_logic;
signal DataIn_3_tb : std_logic;
signal Clk_cont_tb : std_logic;
signal CE_cont : std_logic;
signal RESET   : std_logic;
signal PL : std_logic;

begin

--Unit Under Test
UUT: Mux_Up
	port map (
				S_Ks(0) => S0_Ks_tb
				S_Ks(1) => S1_Ks_tb
				M_Ks(0) => M0_Ks_tb
				M_Ks(1) => M1_Ks_tb
				M_Ks(2) => M2_Ks_tb
				M_Ks(3) => M3_Ks_tb
				Clk   => Clk_tb
				Ym_Ks => Ym_Ks_tb);

clk_gen : process
begin
		Clk_tb <= '0'
		wait for HALF_TIME;
		Clk_tb <= '1'
		wait for HALF_TIME;
end process;

stimulus: process
begin
	S0_Ks_tb <= '0';
	S1_Ks_tb <= '0';
	M0_Ks_tb <= '1';
	M1_Ks_tb <= '0';
	M2_Ks_tb <= '0';
	M3_Ks_tb <= '0';

	wait for TIME_TIME*2;
	
	S0_Ks_tb <= '1';
    	S1_Ks_tb <= '0';
    	M0_Ks_tb <= '0';
   	M1_Ks_tb <= '1';
    	M2_Ks_tb <= '0';
    	M3_Ks_tb <= '0';

    	wait for TIME_TIME*2;
    
    	S0_Ks_tb <= '0';
    	S1_Ks_tb <= '1';
    	M0_Ks_tb <= '0';
    	M1_Ks_tb <= '0';
    	M2_Ks_tb <= '1';
    	M3_Ks_tb <= '0';

    	wait for TIME_TIME*2;
    
     	S0_Ks_tb <= '1';
    	S1_Ks_tb <= '1';
    	M0_Ks_tb <= '0';
   	M1_Ks_tb <= '0';
    	M2_Ks_tb <= '0';
   	M3_Ks_tb <= '1';

        wait for TIME_TIME*2;

	wait;
end process;

end architecture;
	
	

