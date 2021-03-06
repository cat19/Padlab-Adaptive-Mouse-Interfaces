package wekatest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.meta.RotationForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.ClassifierSubsetEval;
import weka.attributeSelection.BestFirst;

public class Wekatest {
	
	//Deletes an attribute by its name
	//Inefficient but it fills a gap in the Weka API
	//Does check to see if you're deleting the class attribute, once it finds a match
	//But for everyone's sanity please don't do that
	//Deleting your class attribute is bad
	//It is case sensitive, so if you have xAccel and xaccel and ask it to delete
	//xaccel, it will ONLY delete xaccel and not xAccel
	
	//deleteByName 2.0 now handles spaces in attribute names
	public static void deleteByName(Instances inst, String attrName){
		String name;
		for(int i=0; i<inst.numAttributes(); i++){
			String namey = inst.attribute(i).toString();
			//the name of the attribute is the second word in the attribute string
			if(namey.contains("'")){
				name = inst.attribute(i).toString().split("'")[1];
			}
			else{
				name = inst.attribute(i).toString().split(" ")[1];
			}
			
			//System.out.println(name);
			if(name.equals(attrName)){
				if(namey.contains("'")){
					if(name.equals(inst.classAttribute().toString().split("'")[1])){
						System.out.println("Can't delete the class attribute");
					}
					else{
						System.out.println("deleting " + name);
						inst.deleteAttributeAt(i);
					}
				}
				else{
					if(name.equals(inst.classAttribute().toString().split(" ")[1])){
						System.out.println("Can't delete the class attribute");
					}
					else{
						System.out.println("deleting " + name);
						inst.deleteAttributeAt(i);
					}
				}
				
				
			}
		}
	}

	//second deletebyname, withholds one instance more than the classattribute
	public static int deleteByName(Instances inst, String attrName, String toWithold){
		String name;
		for(int i=0; i<inst.numAttributes(); i++){
			String namey = inst.attribute(i).toString();
			//the name of the attribute is the second word in the attribute string
			if(namey.contains("'")){
				name = inst.attribute(i).toString().split("'")[1];
			}
			else{
				name = inst.attribute(i).toString().split(" ")[1];
			}
			
			//System.out.println(name);
			if(name.equals(attrName)){
				if(namey.contains("'")){
					if(name.equals(inst.classAttribute().toString().split("'")[1]) || name.equals(toWithold)){
						System.out.println("Can't delete the class attribute");
						return 1;
					}
					else{
						System.out.println("deleting " + name);
						inst.deleteAttributeAt(i);
						return 0;
					}
				}
				else{
					if(name.equals(inst.classAttribute().toString().split(" ")[1]) || name.equals(toWithold)){
						System.out.println("Can't delete the class attribute");
						return 1;
					}
					else{
						System.out.println("deleting " + name);
						inst.deleteAttributeAt(i);
						return 0;
					}
				}
				
				
			}
		}
		return 0;
	}
	
	public static void main(String[] args) throws Exception{

		//make bufferedreader
		/*BufferedReader breader = null;
		breader = new BufferedReader(new FileReader("tex.arff"));

		Instances train = new Instances(breader);
		train.setClassIndex(train.numAttributes() - 1);
		Instances trainx = new Instances(train);
		System.out.println(trainx.numAttributes());
		deleteByName(trainx,"zrotmean");
		for(int i=0; i<(trainx.numAttributes()); i++){
			System.out.println(trainx.attribute(i));
		}
		System.out.println(trainx.numAttributes());
		
		breader.close();

		//NAIVE BAYES
		NaiveBayes nb = new NaiveBayes();
		//tell it what to classify
		nb.buildClassifier(train);
		//tell it what it's evaluating on
		Evaluation eval = new Evaluation(train);
		//crossvalidation with the training data and the classifier
		eval.crossValidateModel(nb, train, 10, new Random(1));
		//printouts
		//basic info
		System.out.println(eval.toSummaryString("======\nResults for NB Tex\n======",true));
		//confusion matrix
		System.out.println(eval.toMatrixString());
		//other stats
		System.out.println("Other: " + eval.fMeasure(1) + " " + eval.precision(1) + " " + eval.recall(1));

		//it's all pretty much the same for the classifiers
		
		//J48
		J48 j48 = new J48();
		j48.buildClassifier(train);
		Evaluation evalj = new Evaluation(train);
		evalj.crossValidateModel(j48,train,10,new Random(1));
		//printouts
		System.out.println(evalj.toSummaryString("\n\n======\nResults for J48 Tex\n======",true));
		System.out.println(evalj.toMatrixString());
		System.out.println("Other: " + evalj.fMeasure(1) + " " + evalj.precision(1) + " " + evalj.recall(1));

		//ROTATION FOREST
		RotationForest rf = new RotationForest();
		rf.buildClassifier(train);
		Evaluation evalr = new Evaluation(train);
		evalr.crossValidateModel(rf,train,10,new Random(1));
		//printouts
		System.out.println(evalr.toSummaryString("\n\n======\nResults for RF Tex\n======",true));
		System.out.println(evalr.toMatrixString());
		System.out.println("Other: " + evalr.fMeasure(1) + " " + evalr.precision(1) + " " + evalr.recall(1));

		//new set - testing without location
		//still all the same
		BufferedReader breader2 = null;
		breader2 = new BufferedReader(new FileReader("NLtex.arff"));

		Instances train2 = new Instances(breader2);
		train2.setClassIndex(train2.numAttributes() - 1);

		breader2.close();

		//NAIVE BAYES (no location)
		NaiveBayes nb2 = new NaiveBayes();
		nb2.buildClassifier(train2);
		Evaluation eval2 = new Evaluation(train2);
		eval2.crossValidateModel(nb2, train2, 10, new Random(1));
		//printouts
		System.out.println(eval2.toSummaryString("\n\n======\nResults for NB TexNL\n======",true));
		System.out.println(eval2.toMatrixString());
		System.out.println("Other: " + eval2.fMeasure(1) + " " + eval2.precision(1) + " " + eval2.recall(1));

		//J48 (no location)
		J48 j482 = new J48();
		j482.buildClassifier(train2);
		Evaluation evalj2 = new Evaluation(train2);
		evalj2.crossValidateModel(j482,train2,10,new Random(1));
		//printouts
		System.out.println(evalj2.toSummaryString("\n\n======\nResults for J48 TexNL\n======",true));
		System.out.println(evalj2.toMatrixString());
		System.out.println("Other: " + evalj2.fMeasure(1) + " " + evalj2.precision(1) + " " + evalj2.recall(1));

		//ROTATION FOREST (no location)
		RotationForest rf2 = new RotationForest();
		rf2.buildClassifier(train2);
		Evaluation evalr2 = new Evaluation(train2);
		evalr2.crossValidateModel(rf2,train2,10,new Random(1));
		//printout
		System.out.println(evalr2.toSummaryString("\n\n======\nResults for RF TexNL\n======",true));
		System.out.println(evalr2.toMatrixString());
		System.out.println("Other: " + evalr2.fMeasure(1) + " " + evalr2.precision(1) + " " + evalr2.recall(1));

		//Classifier Test
		//Testing ClassifierSubsetEval vs CfsSubsetEval
		//Using with location data
		//ClassifierSubsetEval
		ClassifierSubsetEval cse = new ClassifierSubsetEval();
		//set the classifier
		cse.setClassifier(new J48());
		//evaluate on what data?
		cse.buildEvaluator(train);
		//search scheme and how many nonimproving nodes to do before stopping
		BestFirst bf = new BestFirst();
		bf.setSearchTermination(5);
		//printouts
		int[] attributes = bf.search(cse, train);
		System.out.print("\n\nAttributes list from cse: ");
		for(int i=0; i<attributes.length; i++){
			System.out.print(attributes[i] + ",");
		}
		//System.out.println("\n\nNow doing cfs");
		
		//CfsSubsetEval
		CfsSubsetEval cfs = new CfsSubsetEval();
		//Can't specify the classifier to use with this one
		//cfs.setClassifier(new J48());
		//otherwise it's the same
		cfs.buildEvaluator(train);
		BestFirst bf2 = new BestFirst();
		bf2.setSearchTermination(5);
		//printouts
		int[] attributes2 = bf2.search(cfs, train);
		System.out.print("\n\nAttributes list from cfs: ");
		for(int i=0; i<attributes2.length; i++){
			System.out.print(attributes2[i] + ",");
		}

		//Now it's doing it using a meta classifier
		//Does it all in one step and lets you choose what attributes to use
		//With location data
		//Attribute selection for cfs
		System.out.println("\n\nAttribute Selection - Cfs");
		AttributeSelectedClassifier classifier = new AttributeSelectedClassifier();
		CfsSubsetEval eval3 = new CfsSubsetEval();
		BestFirst searchy = new BestFirst();
		J48 basey = new J48();
		classifier.setClassifier(basey);
		classifier.setEvaluator(eval3);
		classifier.setSearch(searchy);
		// 10-fold cross-validation
		Evaluation evaluation = new Evaluation(train);
		evaluation.crossValidateModel(classifier, train, 10, new Random(1));
		System.out.println(evaluation.toSummaryString());
		System.out.println(evaluation.toMatrixString());
		
		//Attribute selection for CSE
		//This one doesn't work so well, as you'll see. Oops.
		System.out.println("\n\n\nAttribute Selection - CSE");
		AttributeSelectedClassifier classifier2 = new AttributeSelectedClassifier();
		ClassifierSubsetEval eval4 = new ClassifierSubsetEval();
		BestFirst searchy2 = new BestFirst();
		J48 basey2 = new J48();
		//You can set the classifier for CSE when using ASC
		classifier2.setClassifier(basey2);
		classifier2.setEvaluator(eval4);
		classifier2.setSearch(searchy2);
		// 10-fold cross-validation
		Evaluation evaluation2 = new Evaluation(train);
		evaluation2.crossValidateModel(classifier2, train, 10, new Random(1));
		System.out.println(evaluation2.toSummaryString());
		System.out.println(evaluation2.toMatrixString());*/
		
		BufferedReader breaders = null;
		//breaders = new BufferedReader(new FileReader("C:\\Users\\Caity\\Dropbox\\adaptive-interfaces\\amy-2010-results\\internet-only\\5-26-noWindowAPI-outliers-noDistLessEuc-uncorrupted-internet-noHugeAPI.arff"));
		breaders = new BufferedReader(new FileReader("C:\\Users\\Caity\\Documents\\PAD\\mouseOutput.arff"));
		

		System.out.println("import");
		
		Instances trains = new Instances(breaders);
		breaders.close();
		trains.setClassIndex(trains.numAttributes() - 1);
		//Testing modified deleteByName to handle spaces in names
		/*for(int i=0; i<(trains.numAttributes()); i++){
			System.out.println(trains.attribute(i));
		}
		System.out.println(trains.numAttributes());
		deleteByName(trains,"CBTDestroyed");
		System.out.println(trains.numAttributes());
		deleteByName(trains,"start menu?");
		System.out.println(trains.numAttributes());
		for(int i=0; i<(trains.numAttributes()); i++){
			System.out.println(trains.attribute(i));
		}*/
		
		//J48 cModel = new J48();  
		//cModel.buildClassifier(trains);  

		//weka.core.SerializationHelper.write("j48blah.model", cModel);

		J48 cls = (J48) weka.core.SerializationHelper.read("C:\\Users\\Caity\\Documents\\GitHub\\Padlab-Adaptive-Mouse-Interfaces\\Weka Models\\ClickDuration_J48.model");
		//J48 cls = (J48) weka.core.SerializationHelper.read("J48blah.model");

		//Delete all unused attributes
		/*for(int i=0; i<trains.numAttributes(); i++){
			String t = trains.attribute(i).toString();
			if(t.contains("'")){
				deleteByName(trains,t.split("'")[1],"clickDuration");
			}
			else{
				deleteByName(trains,t.split(" ")[1],"clickDuration");
			}
		}*/
		int i=0;
		while(i<trains.numAttributes()){
			String t = trains.attribute(i).toString();
			if(t.contains("'")){
				if(deleteByName(trains,t.split("'")[1],"clickDuration") == 1){
					i++;
				}
			}
			else{
				if(deleteByName(trains,t.split(" ")[1],"clickDuration") == 1){
					i++;
				}
			}
		}
		
		System.out.println(trains.numAttributes());
		
		// Test the model
		Evaluation eTest = new Evaluation(trains);
		eTest.evaluateModel(cls, trains);
		System.out.println(eTest.toSummaryString());
		System.out.println(eTest.toMatrixString());
		
		/*eTest.crossValidateModel(cls, trains, 10, new Random(1));
		System.out.println(eTest.toSummaryString());
		System.out.println(eTest.toMatrixString());*/
		
		for(int j=0; j<trains.numInstances(); j++){
			Instance in = trains.instance(j);
			
			//This is the actual data string
			System.out.println(in.toString());
			
			//Instance in = trains.instance(82000);
			//PRINTS OUT 0.0 FOR ABLE
			//1.0 FOR NOT
			//System.out.println(in.attribute(10));
			
			//This is what it's classified as
			/*double res = (cls.classifyInstance(in));
			if(res == 0.0){
				System.out.println("ABLE");
			}
			else{
				System.out.println("NOT");
			}*/
			//What it should return to abdullah's thing is 1.0 or 0.0
			//Method to use for abdullah:
			double res = (cls.classifyInstance(in));
			System.out.println(res);
		}

	}
	
}
