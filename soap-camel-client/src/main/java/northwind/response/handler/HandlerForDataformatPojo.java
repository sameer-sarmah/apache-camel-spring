package northwind.response.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.learnwebservices.services.hello.HelloEndpoint;
import com.learnwebservices.services.hello.HelloResponse;

import northwind.util.Util;

@Component
public class HandlerForDataformatPojo implements IResponseHandler{


	@Override
	public void handle(Object response) {
		//When dataformat=POJO ,which is the default value
		if(response instanceof List) {
			List contents = (List)response;
			if(contents.size() > 0 ) {
				Optional<HelloResponse> responseOptional =  extractResponse(contents);
				responseOptional.ifPresent(helloResponse -> {
					System.out.println(helloResponse.getMessage());
				});
			}
			
		}
		
	}

	@Override
	public boolean canHandle(Object response, Map<String, Object> headers) {
		if(response instanceof List) {
			List contents = (List)response;
			return extractResponse(contents).isPresent();	
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private Optional<HelloResponse> extractResponse(List contents){
		if(contents.size() > 0 ) {
			Optional<HelloResponse> responseOptional =  contents.stream()
																.filter(content -> content instanceof HelloResponse)
																.findFirst();
			return responseOptional;
		}
		return Optional.empty();
	}

}
