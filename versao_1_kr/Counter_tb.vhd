library ieee;
use ieee.std_logic_1164.all;

entity Counter_tb is
end Counter_tb ;

architecture behavioral of Counter_tb is

component Counter is
port(   
	DataIn		: in std_logic_vector(3 downto 0);
	Clk_cont		: in std_logic;
	CE_cont		: in std_logic;
	RESET 		: in std_logic;
	PL 			: in std_logic;
	CQ				: out STD_LOGIC_VECTOR(3 downto 0)
);
end component;	

-- UUT signals
constant TIME_TIME : time := 20 ns;
constant HALF_TIME : time := TIME_TIME / 2;

signal Clk_cont_tb : std_logic;
signal CE_cont_tb : std_logic;
signal RESET_tb   : std_logic;
signal PL_tb : std_logic;
signal CQ0_tb : std_logic;
signal CQ1_tb : std_logic;
signal CQ2_tb : std_logic;
signal CQ3_tb : std_logic;


begin

--Unit Under Test
UUT: Counter
	port map (
				DataIn(0) => '0',
				DataIn(1) => '0',
				DataIn(2) => '0',
				DataIn(3) => '0',
				Clk_cont => Clk_cont_tb,
				CE_cont => CE_cont_tb,
				RESET   => RESET_tb,
				PL => '0',
				CQ(0) => CQ0_tb,	
				CQ(1)=> CQ1_tb,
				CQ(2) => CQ2_tb,
				CQ(3) => CQ3_tb
				);

clk_gen : process
begin
		Clk_cont_tb <= '0';
		wait for HALF_TIME;
		Clk_cont_tb <= '1';
		wait for HALF_TIME;
end process;

stimulus: process
begin
	
   CQ0_tb <= '0';
	CQ1_tb <= '0';
	CQ2_tb <= '0';
	CQ3_tb <= '0';
	CE_cont_tb <= '0';
	RESET_tb <= '1';

	wait for TIME_TIME*2;
	
	CE_cont_tb <= '1';
	RESET_tb <= '0';
	CQ0_tb <= '1';
	CQ1_tb <= '0';
	CQ2_tb <= '0';
	CQ3_tb <= '0';

	
	wait for TIME_TIME*7;
    
    	CQ0_tb <= '0';
	CQ1_tb <= '1';
	CQ2_tb <= '1';
	CQ3_tb <= '0';

	wait for TIME_TIME*16;
    
	CQ0_tb <= '1';
	CQ1_tb <= '1';
	CQ2_tb <= '1';
	CQ3_tb <= '1';

     	wait for TIME_TIME*2;

	CE_cont_tb <= '0';
	RESET_tb <= '1';

	wait;
end process;

end architecture;