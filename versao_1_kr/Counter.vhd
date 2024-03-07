LIBRARY IEEE;
use IEEE.std_logic_1164.all;

ENTITY Counter IS
PORT(
	DataIn : in STD_LOGIC_VECTOR(3 downto 0);
	Clk_cont: in std_logic;
	CE_cont: in std_logic;
	PL: in std_logic;
	RESET: in std_logic;
	
	CQ: out STD_LOGIC_VECTOR(3 downto 0));
	
END Counter;

architecture structural of Counter is

component Registro_Cont
PORT(
	CLK : in std_logic;
	RESET : in STD_LOGIC;
	D : IN STD_LOGIC_VECTOR(3 downto 0);
	EN : IN STD_LOGIC;
	Q : out std_logic_VECTOR(3 downto 0));
end component;



component ADD_Cont
PORT ( 
	Q: in std_logic_vector(3 downto 0);
	B: in std_logic_vector(3 downto 0);
	C0 : in std_logic;
	S: out std_logic_vector(3 downto 0);
	C4: out std_logic);
end component;

component MUX_Cont
port(
    A, B : in STD_LOGIC_VECTOR(3 downto 0);
    S : in STD_LOGIC;
    Y : out STD_LOGIC_VECTOR(3 downto 0));
end component;	

signal R_CUP, R_RES, R_ADD, R_MUX : std_logic_vector(3 downto 0);

begin 
U1: Registro_Cont port map (
	CLK => CLK, 
	reset => reset,
	D => R_RES, 
	EN => CE, 
	Q => R_ADD);

Q <= R_ADD;
	

U2: ADD_Cont port map (
	Q => R_ADD, 
	B=>"0001", 
	C0 => '0', 
	S => R_MUX, 
	C4 => Open);
	

U3: MUX_Cont port map (
	A => dataIN,
	B => R_MUX,
	S => PL,
	y => R_RES);


	
end structural;