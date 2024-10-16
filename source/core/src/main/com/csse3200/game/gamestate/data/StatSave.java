package com.csse3200.game.gamestate.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.csse3200.game.components.stats.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatSave implements Json.Serializable {
    Logger logger = LoggerFactory.getLogger(StatSave.class);
    public Array<Stat> stats = new Array<>();

    @Override
    public void write(Json json) {
        json.writeArrayStart("stats");
        for(Stat element : stats) {
            json.writeObjectStart();
            json.writeType(element.getClass());
            json.writeValue("statName", element.getStatName());
            json.writeValue("statDescription", element.getStatDescription());
            json.writeValue("statCurrent", element.getCurrent());
            json.writeValue("statHasMax", element.getStatMax()!=-1);
            json.writeValue("statMax", element.getStatMax());
            json.writeValue("type", element.getType());
            json.writeObjectEnd();
        }
        json.writeArrayEnd();
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        JsonValue.JsonIterator iterator = jsonData.child.iterator();
        while(iterator.hasNext()) {
            JsonValue value = iterator.next();
            Stat newStat = new Stat(
                    value.get("statName").asString(),
                    value.get("statDescription").asString(),
                    value.get("statCurrent").asInt(),
                    value.get("statMax").asInt(),
                    value.get("statHasMax").asBoolean(),
                    Stat.StatType.valueOf(value.get("type").asString())
            );
            stats.add(newStat);
        }
    }
}
