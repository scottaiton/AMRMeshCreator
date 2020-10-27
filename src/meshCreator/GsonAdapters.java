package meshCreator;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonAdapters {
	private static class SideAdapter implements JsonSerializer<Side>, JsonDeserializer<Side> {
		@Override
		public JsonElement serialize(Side src, Type srcType, JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public Side deserialize(JsonElement json, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			try {
				return Side.fromString(json.getAsString());
			} catch (IllegalArgumentException e) {
				// May be it came in formatted as a plain Side class, so try that
				Side side = context.deserialize(json, Side.class);
				return side;
			}
		}
	}

	private static class OrthantAdapter implements JsonSerializer<Orthant>, JsonDeserializer<Orthant> {
		@Override
		public JsonElement serialize(Orthant src, Type srcType, JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public Orthant deserialize(JsonElement json, Type type, JsonDeserializationContext context)
				throws JsonParseException {
			try {
				return Orthant.fromString(json.getAsString());
			} catch (IllegalArgumentException e) {
				// May be it came in formatted as a plain Side class, so try that
				Orthant orth = context.deserialize(json, Orthant.class);
				return orth;
			}
		}
	}

	public static Gson getNewGson() {
		GsonBuilder gson = new GsonBuilder();
		gson.setPrettyPrinting();
		gson.registerTypeAdapter(Side.class, new SideAdapter());
		gson.registerTypeAdapter(Orthant.class, new OrthantAdapter());
		return gson.create();
	}

}
