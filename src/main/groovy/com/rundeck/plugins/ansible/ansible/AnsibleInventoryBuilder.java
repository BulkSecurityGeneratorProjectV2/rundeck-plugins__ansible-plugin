package com.rundeck.plugins.ansible.ansible;

import com.dtolabs.rundeck.core.common.INodeEntry;
import com.dtolabs.rundeck.core.plugins.configuration.ConfigurationException;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;

public class AnsibleInventoryBuilder {

    private final Collection<INodeEntry> nodes;

    public AnsibleInventoryBuilder(Collection<INodeEntry> nodes) {
        this.nodes = nodes;
    }

    public File buildInventory() throws ConfigurationException {
        try {
            File file = Files.createTempFile("ansible-inventory", ".json").toFile();
            file.deleteOnExit();
            PrintWriter writer = new PrintWriter(file);
            AnsibleInventory ai = new AnsibleInventory();
            for (INodeEntry e : nodes) {
                ai.addHost(e.getNodename(), e.getHostname(), new HashMap<String, String>(e.getAttributes()));
            }
            writer.write(new Gson().toJson(ai));
            writer.close();
            return file;
        } catch (Exception e) {
            throw new ConfigurationException("Could not write temporary inventory: " + e.getMessage());
        }
    }
}
