package net.nilsghesquiere.security;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;



@Service
public class LoginAttemptService {
 
    private final int MAX_ATTEMPT = 10;
    private LoadingCache<String, Integer> attemptsCache;
    private LoadingCache<String, Set<String>> usernamesCache;
 
    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
          expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
        
        usernamesCache = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Set<String>>() {
                  public Set<String> load(String key) {
                      return new LinkedHashSet<>();
                  }
              });
    }
 
    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
        usernamesCache.invalidate(key);
    }
 
    public void loginFailed(String key, String username) {
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
        try {
			Set<String>  usernames =usernamesCache.get(key);
			usernames.add(username);
			usernamesCache.put(key, usernames);
		} catch (ExecutionException e) {
			Set<String> usernames = new LinkedHashSet<>();
			usernames.add(username);
			usernamesCache.put(key, usernames);
		}
    }
 
    public boolean isBlocked(String key) {
        try {
            return attemptsCache.get(key) >= MAX_ATTEMPT;
        } catch (ExecutionException e) {
            return false;
        }
    }
    
   public LoadingCache<String, Integer> findAllAttempts(){
	   return attemptsCache;
   }

   public LoadingCache<String, Set<String>> findAllUsernames(){
	   return usernamesCache;
   }
}