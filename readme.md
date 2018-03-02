<h1>LeagueInformer base project</h1>
<h2>InfernalManager</h2>
<h3>TODO:</h3>	
<ul>
<li>Test anything registration & email related</li>
<li>Complete spring security with REST (http://www.baeldung.com/security-spring)</li>
<li><del>Change Account entity to better match the entity in Infernalbot (Inheritance?)</del></li>
<li><del>Make a Settings entity matching settings in Infernalbot</del></li>
<li><del>Update entities on client</del></li>
<li><del>Update methods on client</del></li>
<li>Fix ENUMs usage in REST</li>
<li>Map out how the client - infernalbot - server connection / exchange should work</li>
<li>Client JDBC connection to SQLite file</li>
<li>Client settings</li>
<li>Data auditing for database</li>
<li>Temp frontend: botostrap and basic js/jquery </li>
<li>Better frontend (Angular?)</li>
</ul>

<h3>Client server connection map(for LolAccounts</h3>
<ol>
<li>Client connects to Infernalbot database and reads the accounts</li>
<li>Loop over the grabbed accounts, launch REST query for each account (get id by accountignorecase) fill in ID's or null</li>
<li>Wrap the Accounts in an accountmap (figure out what value to use for new accounts)</li>
<li>Send to a seperate method in the LolAccount REST controller of the server</li>
<li>For new accounts: create the account with AccountStatus NEW, for existing: update account and set AccountStatus according to checks</li>
<li>If this send a success response: delete accounts in infernal DB</li>
<li>Grab new accounts for use from the server according to client parameters, update status to in use and assigned to to client name<li>
<li>Insert new accounts for use into the infernal database</li>
</ol>

<h3>Client server connection map(for InfernalSettings</h3>
<ol>
<li>Client connects to Server and grabs InfernalSettings</li>
<li>Client connects to Infernalbot database and reads the default settings(only the ones we don't make editable)</li>
<li>Client puts the default settings into the settings recieved form the server</li>
<li>Client checks if there is a Settings entry with the name InfernalManager: Yes -> Update; No -> Create</li>
<li>Set the ID of the infernalmanager settings as the active ones</li>
</ol>