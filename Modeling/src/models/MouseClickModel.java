package models;

import java.io.BufferedReader;
import java.io.FileReader;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instance;
import weka.core.Instances;

public class MouseClickModel {
	
	static int debug = 1;

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
						if(debug==1){
							System.out.println("Can't delete the class attribute");
						}
					}
					else{
						if(debug==1){
							System.out.println("deleting " + name);
						}
						inst.deleteAttributeAt(i);
					}
				}
				else{
					if(name.equals(inst.classAttribute().toString().split(" ")[1])){
						if(debug==1){
							System.out.println("Can't delete the class attribute");
						}
					}
					else{
						if(debug==1){
							System.out.println("deleting " + name);
						}
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
						if(debug==1){
							System.out.println("Can't delete the class attribute");
						}
						return 1;
					}
					else{
						if(debug==1){
							System.out.println("deleting " + name);
						}
						inst.deleteAttributeAt(i);
						return 0;
					}
				}
				else{
					if(name.equals(inst.classAttribute().toString().split(" ")[1]) || name.equals(toWithold)){
						if(debug==1){
							System.out.println("Can't delete the class attribute");
						}
						return 1;
					}
					else{
						if(debug==1){
							System.out.println("deleting " + name);
						}
						inst.deleteAttributeAt(i);
						return 0;
					}
				}
				
				
			}
		}
		return 0;
	}
	
	public static void main(String[] args) throws Exception{
		
		//Opens the data file for reading
		BufferedReader breaders = null;
		breaders = new BufferedReader(new FileReader("C:\\Users\\Caity\\Documents\\PAD\\mouseOutput.arff"));
		
		if(debug==1){
			System.out.println("import");
		}
		
		//Makes the instances for testing
		Instances trains = new Instances(breaders);
		breaders.close();
		trains.setClassIndex(trains.numAttributes() - 1);
		
		//import the model file
		J48 cls = (J48) weka.core.SerializationHelper.read("C:\\Users\\Caity\\Documents\\GitHub\\Padlab-Adaptive-Mouse-Interfaces\\Weka Models\\ClickDuration_J48.model");
		
		//clean the data file to only include attributes in the model
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
		
		if(debug==1){
			System.out.println(trains.numAttributes());
		}
				
		// Test the model
		Evaluation eTest = new Evaluation(trains);
		eTest.evaluateModel(cls, trains);
		
		if(debug==1){
			System.out.println(eTest.toSummaryString());
			System.out.println(eTest.toMatrixString());
		}
				
		for(int j=0; j<trains.numInstances(); j++){
			Instance in = trains.instance(j);
			
			//This is the actual data string
			if(debug==1){
				System.out.println(in.toString());
			}
			
			//PRINTS OUT 0.0 FOR ABLE
			//1.0 FOR NOT
						
			//This is what it's classified as
			if(debug==1){
				double res = (cls.classifyInstance(in));
				if(res == 0.0){
					System.out.println("ABLE");
				}
				else{
					System.out.println("NOT");
				}
			}
			
			//What it should return to abdullah's thing is 1.0 or 0.0
			//Method to use for the interface:
			if(debug==0){
				double res = (cls.classifyInstance(in));
				System.out.println(res);
			}
		}
	}
	
	
}
