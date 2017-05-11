package com.example.bilel.bluetoothfirstapp;



import android.util.Log;

import com.bayesserver.*;
import com.bayesserver.data.*;
import com.bayesserver.inference.*;
import com.bayesserver.learning.parameters.*;

import java.util.Arrays;

public class PLearn {

    public PLearn() {

        Network network = CreateNetworkStructure();  // we manually construct the network here, but it could be loaded from a file
        Variable x = network.getVariables().get("X");
        Variable y = network.getVariables().get("Y");

        // now learn the parameters from the data in Walkthrough 2 - Mixture model

        // This example uses Sql Server as the data source and assumes the data has been copied to
        // a table called MixtureModel
        // We will use the RelevanceTree algorithm here, as it is optimized for parameter learning
        ParameterLearning learning = new ParameterLearning(network, new RelevanceTreeInferenceFactory());
        ParameterLearningOptions learningOptions = new ParameterLearningOptions();

        String connectionUrl = "<connection url to database goes here>";

        DataReaderCommand dataReaderCommand = new DatabaseDataReaderCommand(
                connectionUrl,
                "Select X, Y From MixtureModel");   // note that if a case column was added we would need to ORDER BY the case column here


        ReaderOptions readerOptions = new ReaderOptions(null);    // we do not have a case column in this example

        // here we map variables to database columns
        // in this case the variables and database columns have the same name
        VariableReference[] variableReferences = new VariableReference[]
                {
                        new VariableReference(x, ColumnValueType.VALUE, x.getName()),
                        new VariableReference(y, ColumnValueType.VALUE, y.getName())
                };

        // note that although this example only has non temporal data
        // we could have included additional temporal variables and data

        EvidenceReaderCommand evidenceReaderCommand = new DefaultEvidenceReaderCommand(
                dataReaderCommand,
                Arrays.asList(variableReferences),
                readerOptions);

        ParameterLearningOutput result = learning.learn(evidenceReaderCommand, learningOptions);

        Log.d(Constants.TAG, "LEARNING PARA: " + "Log likelihood = " + result.getLogLikelihood());

    }

    private static Network CreateNetworkStructure() {
        Network network = new Network();

        Node nodeCluster = new Node("Cluster", new String[]{"Cluster1", "Cluster2", "Cluster3"});
        network.getNodes().add(nodeCluster);

        Variable x = new Variable("X", VariableValueType.CONTINUOUS);
        Variable y = new Variable("Y", VariableValueType.CONTINUOUS);

        Node nodePosition = new Node("Position", new Variable[]{x, y});
        network.getNodes().add(nodePosition);

        network.getLinks().add(new Link(nodeCluster, nodePosition));

        // at this point the Bayesian network structure is fully specified

        return network;
    }
}