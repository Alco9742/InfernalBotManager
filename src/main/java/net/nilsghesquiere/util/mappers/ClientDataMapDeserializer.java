package net.nilsghesquiere.util.mappers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.nilsghesquiere.entities.ClientData;
import net.nilsghesquiere.entities.Queuer;
import net.nilsghesquiere.entities.QueuerLolAccount;
import net.nilsghesquiere.util.enums.Lane;
import net.nilsghesquiere.util.wrappers.ClientDataMap;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class ClientDataMapDeserializer extends JsonDeserializer<ClientDataMap>{
	
	@Override
	public ClientDataMap deserialize(JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException {
		ClientDataMap map = new ClientDataMap();
		ObjectCodec oc = jp.getCodec();	
		JsonNode node = oc.readTree(jp);
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
		while (fieldsIterator.hasNext()){
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			JsonNode clientDataNode = field.getValue();
			final String key =  field.getKey();
			final Long clientDataId = clientDataNode.get("id").asLong();
			JsonNode queuerNodes = clientDataNode.get("queuers");
			List<Queuer> queuers = new ArrayList<>();
			for (JsonNode queuerNode : queuerNodes){
				final Long queuerId = queuerNode.get("id").asLong();
				final String queuerQueuer = queuerNode.get("queuer").asText();
				final Boolean queuerSoftEnd = queuerNode.get("softEnd").asBoolean();
				final Integer queuerAfterGame = queuerNode.get("afterGame").asInt();
				final Integer queuerPlayedGames = queuerNode.get("playedGames").asInt();
				final Integer queuerWinGames = queuerNode.get("winGames").asInt();
				final Integer queuerDefeatGames = queuerNode.get("defeatGames").asInt();
				final String queuerState = queuerNode.get("state").asText();
				final Boolean queuerLpq = queuerNode.get("lpq").asBoolean();
				JsonNode queuerAccountNodes = queuerNode.get("queuerLolAccounts");
				List<QueuerLolAccount> lolAccounts = new ArrayList<>();
				for (JsonNode queuerAccountNode : queuerAccountNodes){
					final Long queuerAccountId = queuerAccountNode.get("id").asLong();
					final String queuerAccountAccount = queuerAccountNode.get("account").asText();
					final Integer queuerAccountLevel = queuerAccountNode.get("level").asInt();
					final Integer queuerAccountMaxLevel = queuerAccountNode.get("maxLevel").asInt();
					final Integer queuerAccountXp = queuerAccountNode.get("xp").asInt();
					final Integer queuerAccountXpCap = queuerAccountNode.get("xpCap").asInt();
					final Integer queuerAccountBe = queuerAccountNode.get("be").asInt();
					final String queuerAccountChamp= queuerAccountNode.get("champ").asText();
					final String queuerAccountLaneString= queuerAccountNode.get("lane").asText();
					final Boolean queuerAccountLpq= queuerAccountNode.get("lpq").asBoolean();
					QueuerLolAccount lolAccount = new QueuerLolAccount(queuerAccountId,queuerAccountAccount, queuerAccountLevel,queuerAccountMaxLevel,queuerAccountXp,queuerAccountXpCap, queuerAccountBe, queuerAccountChamp, Lane.valueOf(queuerAccountLaneString), queuerAccountLpq);
					lolAccounts.add(lolAccount);
				}
				Queuer queuer = new Queuer(queuerId,queuerQueuer,queuerSoftEnd,queuerAfterGame,queuerPlayedGames,queuerWinGames,queuerDefeatGames, queuerState,lolAccounts,queuerLpq);
				queuers.add(queuer);
			}
			final String clientDataDateString = clientDataNode.get("date").asText();
			final LocalDateTime clientDataDate = LocalDateTime.parse(clientDataDateString);
			final String clientDataStatus = clientDataNode.get("status").asText();
			final String clientDataRamInfo = clientDataNode.get("ramInfo").asText();
			final String clientDataCpuInfo = clientDataNode.get("cpuInfo").asText();
			ClientData clientData = new ClientData(clientDataId,queuers,clientDataDate,clientDataStatus,clientDataRamInfo,clientDataCpuInfo);
			map.add(key, clientData);
		}
		return map;
	}

}
