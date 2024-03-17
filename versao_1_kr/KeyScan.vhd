LIBRARY IEEE;
use IEEE.std_logic_1164.all;

ENTITY KeyScan IS
PORT(	
	Lin : in STD_LOGIC_VECTOR(3 downto 0);
	Clk: in std_logic;
	Kscan: in std_logic;
	Clk: in std_logic;
	Reset: in std_logic;
	
	Col : out STD_LOGIC_VECTOR(3 downto 0);
	Kpress: out std_logic;
	K : out STD_LOGIC_VECTOR(3 downto 0);

END KeyScan;

architecture structural of KeyScan is

component Counter
PORT(
	DataIn : in STD_LOGIC_VECTOR(3 downto 0);
	Clk_cont: in std_logic;
	CE_cont: in std_logic;
	PL: in std_logic;
	RESET: in std_logic;
	CQ: out STD_LOGIC_VECTOR(3 downto 0));
	
End component;


component DECODER_KS
PORT(
	S_dec: in STD_LOGIC_VECTOR(1 downto 0);
	D_dec: out STD_LOGIC_VECTOR(2 downto 0)
	);
	
end component;
	

component MUX_KS
PORT(
	M_KS: in STD_LOGIC_VECTOR(3 downto 0);
	S_KS: in std_logic_vector(1 downto 0);
	Ym_KS: out std_logic
	);
END component;


signal CQ0-S0_KS : std_logic;

signal CQ1-S1_KS: std_logic;

signal CQ2-S0_dec: std_logic;

signal CQ3-S1_dec: std_logic;



begin



U1: Counter port map (

	DataIn => "0000",
	Clk_cont => Clk,
	CE_cont => Kscan,
	PL => '0',
	RESET => Reset,
	CQ(0) => CQ0-S0_KS,
	CQ(1) => CQ1-S1_KS,
	CQ(2) => CQ2-S0_dec,
	CQ(3) => CQ3-S1_dec
	
	);
	
U2: DECODER_KS port map (
	S_dec(0) => CQ2-S0_dec,
	S_dec(1) => CQ3-S1_dec,
	
	D_dec(0) =>  Col(0), 
	D_dec(1) => Col(1),
	D_dec(2) => Col(2),
	D_dec(3) =>	open
	);

U3: MUX_KS port map (
	M_KS(0) => Lin(0),  
	M_KS(1) => Lin(1), 
	M_KS(2) => Lin(2), 
	M_KS(3) => Lin(3),
	
	S_KS(0) => CQ0-S0_KS,
	S_KS(1) => CQ1-S1_KS,
	Ym_KS => Kpress
	);


end structural;