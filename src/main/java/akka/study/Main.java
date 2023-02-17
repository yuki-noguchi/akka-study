package akka.study;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;

public class Main extends AllDirectives {

	public static void main(String[] args) throws IOException {
		ActorSystem<Void> system = ActorSystem.create(Behaviors.empty(), "routes");

		Http http = Http.get(system);

		Main app = new Main();

		CompletionStage<ServerBinding> binding = http.newServerAt("localhost", 8080).bind(app.createRoute());

		System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
		System.in.read(); // let it run until user presses return

		binding.thenCompose(ServerBinding::unbind) // trigger unbinding from the port
				.thenAccept(unbound -> system.terminate()); // and shutdown when done
	}

	private Route createRoute() {
		return concat(path("hello", () -> get(() -> complete("<h1>Say hello to akka-http</h1>"))));
	}
}
