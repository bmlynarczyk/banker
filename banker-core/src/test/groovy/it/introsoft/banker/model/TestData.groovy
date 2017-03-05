package it.introsoft.banker.model

class TestData {

    static final List<String> MILLENIUM_CARD_PAYMENTS_TRANSFER_LINES = [
            'Typ operacji TRANSAKCJA KARTĄ PŁATNICZĄ',
            'Dzienny numer transakcji 3',
            'Data księgowania 2016-12-09',
            'Data waluty 2016-12-09',
            'Z rachunku 98116022020000000000000000',
            'Karta Visa Konto 360',
            'Numer karty XXXXXXXXXXXXXXXX',
            'Posiadacz karty JAN KOWALSKI',
            'Kwota transakcji 15,10 PLN',
            'Kwota rozliczenia 15,10 PLN',
            'Kwota zaksięgowana -15,10 PLN',
            'Tytuł Stolowka Lublin 16/12/07'
    ]

    static final List<String> M_BANK_DEPOSIT = [
            'Rachunek: Winien (Nadawca) Rachunek: Ma (Odbiorca)',
            'Nr Rachunku: 11 1020 3176 0000 0000 0000 0000 Nr Rachunku: 57 1140 2004 0000 0000 0000 0000',
            'Nazwa Banku: PKOBP OddziaŃ. 4 w Lublinie Nazwa Banku: MBANK S.A.',
            'Nadawca: JAN KOWALSKI Odbiorca: JAN KOWALSKI',
            'WS. TRAWNIKI 320 A',
            '21-044 TRAWNIKI',
            'Tytuł operacji: FAKTURA TELEFON',
            'Rodzaj operacji: PRZELEW ZEWNĘTRZNY PRZYCHODZĄCY',
            'Nr referencyjny operacji: 76317802-000000002',
            'Data operacji: 2016-07-28',
            'Data księgowania: 2016-07-28',
            'Kwota przelewu: 2 100,00 PLN',
    ]
    static final List<String> BGZ_OPTIMA_MULTI_TRANSFER_PAGE = [
            'Lp Data Nazwa transakcji i opis Kwota Saldo po operacji',
            '186 30.04.2013 ODSETKI OTRZYMANE 15,20 PLN 5 016,98 PLN',
            'XXXXXXXXXXXXXXXX',
            'Data waluty: 01.05.2013',
            '187 30.04.2013 ODSETKI OTRZYMANE 1,78 PLN 5 001,78 PLN',
            'XXXXXXXXXXXXXXXX',
            'Data waluty: 01.05.2013',
            '188 03.04.2013 PRZELEW 552,93 PLN 5 000,00 PLN',
            'JAN KOWALSKI',
            '54200084337676308908158304',
            'POZOSTAŁOŚCI 03.2013',
            'XXXXXXXXXXXXXXXX',
            '189 29.03.2013 PODATEK POBRANY -3,07 PLN 4 447,07 PLN',
            'XXXXXXXXXXXXXXXX',
            'Data waluty: 01.04.2013',
            '190 29.03.2013 ODSETKI OTRZYMANE 16,11 PLN 4 450,14 PLN',
            'XXXXXXXXXXXXXXXX',
            'Data waluty: 01.04.2013',
            '191 25.03.2013 PRZELEW 400,00 PLN 4 434,03 PLN',
            'JAN KOWALSKI',
            '54200084337676308908158304',
            'OSZCZĘDNOŚCI 03.2013',
            'XXXXXXXXXXXXXXXX',
            '192 06.03.2013 PRZELEW 1,41 PLN 4 034,03 PLN',
            'JAN KOWALSKI',
            '54200084337676308908158304',
            'ODSETKI Z MBANK 02.2013',
            'XXXXXXXXXXXXXXXX',
            '193 28.02.2013 PODATEK POBRANY -2,04 PLN 4 032,62 PLN',
            'XXXXXXXXXXXXXXXX',
            'Data waluty: 01.03.2013',
            '194 28.02.2013 ODSETKI OTRZYMANE 10,25 PLN 4 034,66 PLN',
            'XXXXXXXXXXXXXXXX',
            'Data waluty: 01.03.2013',
            '195 28.02.2013 ODSETKI OTRZYMANE 0,45 PLN 4 024,41 PLN',
            'XXXXXXXXXXXXXXXX',
            'Data waluty: 01.03.2013',
            '196 25.02.2013 PRZELEW 400,00 PLN 4 023,96 PLN',
            'JAN KOWALSKI',
            '54200084337676308908158304',
            'OSZCZEDNOSCI 02/2013',
            'XXXXXXXXXXXXXXXX',
            '197 07.02.2013 PRZELEW 3 621,96 PLN 3 623,96 PLN',
            'JAN KOWALSKI',
            '54200084337676308908158304',
            'WYCOFANIE SRODKOW',
            'XXXXXXXXXXXXXXXX',
            '198 06.02.2013 PRZELEW 2,00 PLN 2,00 PLN',
            'JAN KOWALSKI',
            '54200084337676308908158304',
            'OPLATA AKTYWACYJNA',
            'XXXXXXXXXXXXXXXX',
            '*kwota - kwota przed przewalutowaniem lub kwota po przewalutowaniu',
            'Uwaga! Data waluty jest prezentowana tylko wówczas, gdy jest inna niż data księgowania (data realizacji).',
            'Jeżeli w opisie transakcji, której przedmiotem są opłaty lub prowizje, brak jest informacji o odbiorcy, środki transakcji pobierane są przez',
            'Bank BGŻ BNP Paribas S.A.',
            'Data wystawienia dokumentu: 15.12.2016, 23:13:00 Strona 12'
    ]

    public static final String PKO_BP_TRANSFER_TABLE = '''
    <html>
    <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="Author" content="PKOInteligo (www.pkointeligo.pl)">
    </head>
<body><script>window.sessionData = {sid: 'xxx:', lifetime: 'xxx'};</script>
    <h3>Kryteria wyszukiwania</h3>
<table>
<tr><td>Numer rachunku</td><td>xxx</td></tr>
    <tr><td>Od dnia</td><td>2005-11-18</td></tr>
<tr><td>Do dnia</td><td>2016-12-18</td></tr>
    <tr><td>Rodzaj operacji</td><td>Wszystkie</td></tr>


<table>
<h3>Lista transakcji</h3>
    <table>
    <tr><td nowrap>Data operacji</td><td nowrap>Data waluty</td><td>Typ transakcji</td><td>Opis transakcji</td><td nowrap>Kwota</td><td>Waluta</td><td>Saldo po transakcji</td></tr>

    <tr>
    <td nowrap>2016-12-16</td>
	<td nowrap>2016-12-16</td>
    <td>Przelew na rachunek</td>
	<td>
	
		Rachunek odbiorcy
		: 
		
			57 1140 2004 0000 0000 0000 0000
		
		<br>
	
		Nazwa odbiorcy
		: 
		
			
				JAN NOWAK
			
		
		<br>
	
		Adres odbiorcy
		: 
		
			
				XXX
			
				XXX
			
		
		<br>
	
		Tytuł
		: 
		
			
				XXX
			
				
			
				
			
				
			
		
		<br>
	
	</td>
    <td nowrap>+2500.00</td>
	<td>PLN</td>
    <td nowrap>+3082.01</td>
	</tr>

    <tr>
    <td nowrap>2016-12-16</td>
	<td nowrap>2016-12-16</td>
    <td>Przelew na rachunek</td>
	<td>
	
		Rachunek odbiorcy
		: 
		
			52 2490 1057 0000 0000 0000 0000
		
		<br>
	
		Nazwa odbiorcy
		: 
		
			
				Jan Nowak
			
		
		<br>
	
		Adres odbiorcy
		: 
		
			
				Saska 44 , 20-315 lublin


    <br>

    Tytuł
    :


    reszta








    <br>

    </td>
	<td nowrap>+158.44</td>
    <td>PLN</td>
	<td nowrap>+582.01</td>
    </tr>

	<tr>
	<td nowrap>2016-12-16</td>
    <td nowrap>2016-12-16</td>
	<td>Przelew na rachunek</td>
    <td>

    Rachunek odbiorcy
    :

    10 2490 1057 0000 0000 0000 0000

    <br>

    Nazwa odbiorcy
    :


    Jan Nowak


    <br>

    Adres odbiorcy
    :


    Saska 434 , 20-023 Lublin


    <br>

    Tytuł
    :


    resztki








    <br>

    </td>
	<td nowrap>+118.69</td>
    <td>PLN</td>
	<td nowrap>+423.57</td>
    </tr>
</table>
    '''
}
