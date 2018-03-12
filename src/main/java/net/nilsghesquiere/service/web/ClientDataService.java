package net.nilsghesquiere.service.web;

import java.util.List;

import net.nilsghesquiere.entities.ClientData;

public interface ClientDataService {
	ClientData read(Long id);
	ClientData create(ClientData clientData);
	ClientData update(ClientData clientData);
	void delete(ClientData clientData);
	void deleteById(Long id);
	List<ClientData> findByUserId(Long userid);
	ClientData findByTagAndUserId(String tag, Long userid);
}
