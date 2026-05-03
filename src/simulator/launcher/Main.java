package simulator.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.DefaultRegionBuilder;
import simulator.factories.DynamicSupplyRegionBuilder;
import simulator.factories.Factory;
import simulator.factories.SelectClosestBuilder;
import simulator.factories.SelectFirstBuilder;
import simulator.factories.SelectYoungestBuilder;
import simulator.factories.SheepBuilder;
import simulator.factories.WolfBuilder;
import simulator.misc.*;
import simulator.model.*;
import simulator.strategies.*;
import simulator.view.MainWindow;
import javax.swing.*;

public class Main {

	private enum ExecMode {
		BATCH("batch", "Batch mode"), GUI("gui", "Graphical User Interface mode");

		private String tag;
		private String desc;

		private ExecMode(String modeTag, String modeDesc) {
			tag = modeTag;
			desc = modeDesc;
		}

		public String getTag() {
			return tag;
		}

		public String getDesc() {
			return desc;
		}
	}

	// default values for some parameters
	//
	private final static Double DEFAULT_TIME = 10.0; // in seconds
	private final static Double DEFAULT_DTIME = 0.03; // in seconds
	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double time = null;
	private static String inFile = null;
	private static String outFile = null;
	private static ExecMode mode = ExecMode.GUI;
	public static Factory<Animal> animalsFactory;
	public static Factory<Region> regionsFactory;
	public static double dt = 0.03;
	public static boolean sv = false;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseModeOption(line);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseDTimeOption(line);
			parseTimeOption(line);
			parseSvOption(line);
			

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("A configuration file.").build());

		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("An output file.").build());

		// steps
		cmdLineOptions.addOption(Option.builder("t").longOpt("time").hasArg()
				.desc("A real number representing the total simulation time in seconds. Default value: "
						+ DEFAULT_TIME + ".")
				.build());

		// steps
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A real number representing the dt. Default value: "
						+ DEFAULT_DTIME + ".")
				.build());


		// sv
		cmdLineOptions.addOption(Option.builder("sv").longOpt("sv").desc("Simple Viewer.").build());
		// sv
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Execution mode.").build());
		
		return cmdLineOptions;
	}

	private static void initFactories() {

		// initialize the strategies factory  
		List<Builder<SelectionStrategy>> selectionStrategyBuilders = new ArrayList<>();  
		selectionStrategyBuilders.add(new SelectFirstBuilder());  
		selectionStrategyBuilders.add(new SelectClosestBuilder()); 
		selectionStrategyBuilders.add(new SelectYoungestBuilder());
		Factory<SelectionStrategy> selectionStrategyFactory = new BuilderBasedFactory<SelectionStrategy>(selectionStrategyBuilders);

		List<Builder<Animal>> animalBuilders = new ArrayList<>();
		animalBuilders.add(new SheepBuilder(selectionStrategyFactory));
		animalBuilders.add(new WolfBuilder(selectionStrategyFactory));

		animalsFactory = new BuilderBasedFactory<>(animalBuilders);

		List<Builder<Region>> regionBuilders = new ArrayList<>();
		regionBuilders.add(new DefaultRegionBuilder());
		regionBuilders.add(new DynamicSupplyRegionBuilder());

		regionsFactory = new BuilderBasedFactory<>(regionBuilders);
	}

	private static void startBatchMode() throws Exception {

		InputStream is = new FileInputStream(new File(inFile));
		JSONObject input = loadJSONFile(is);

		OutputStream os = null;
		if(outFile!=null) {
			os = new FileOutputStream(new File(outFile));
		}else {
			os = System.out;
		}
		
		//TODO: Create an OutputStream similar to InputStream with the value  bound to outFIle

		int cols = input.getInt("cols");
		int rows = input.getInt("rows");
		int width = input.getInt("width");
		int height = input.getInt("height");

		Simulator sim = new Simulator(
				cols, rows, width, height,
				animalsFactory,
				regionsFactory
				);

		Controller ctrl = new Controller(sim);

		ctrl.loadData(input);

		ctrl.run(time, dt, sv, os);
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseSvOption(CommandLine line) {
		if (line.hasOption("sv")) {
			sv = true;
		}
	}
	
	
	private static void parseModeOption(CommandLine line) throws ParseException {
		String m =  line.getOptionValue("m");
		if (m.equalsIgnoreCase(ExecMode.BATCH.getTag())) {
			mode = ExecMode.BATCH;
		}else
			mode = ExecMode.GUI;
	}
	
	private static void parseInFileOption(CommandLine line) throws ParseException {
		inFile = line.getOptionValue("i");
		if (mode == ExecMode.BATCH && inFile == null) {
			throw new ParseException("In batch mode an input configuration file is required");
		}
	}

	
	private static void parseOutFileOption(CommandLine line) throws ParseException {
		outFile = line.getOptionValue("o");

	}
	
	private static void parseTimeOption(CommandLine line) throws ParseException {
		String t = line.getOptionValue("t", DEFAULT_TIME.toString());
		try {
			time = Double.parseDouble(t);
			assert (time >= 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}

	
	private static void parseDTimeOption(CommandLine line) throws ParseException {
		String t = line.getOptionValue("dt", DEFAULT_DTIME.toString());
		try {
			dt = Double.parseDouble(t);
			assert (dt > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid value for time: " + t);
		}
	}
	
	private static JSONObject loadJSONFile(InputStream in) {
		return new JSONObject(new JSONTokener(in));
	}

	private static void startGUIMode() throws Exception {

		int cols = 20;
		int rows = 15;
		int width = 600;
		int height = 800;
		JSONObject input = null;
		if(inFile!=null) {
			InputStream is = new FileInputStream(new File(inFile));
			input = loadJSONFile(is);

			cols = input.getInt("cols");
			rows = input.getInt("rows");
			width = input.getInt("width");
			height = input.getInt("height");
		}

		Simulator sim = new Simulator(
				cols, rows, width, height,
				animalsFactory,
				regionsFactory
				);

		Controller ctrl = new Controller(sim);

		if(inFile != null)
			ctrl.loadData(input);


		SwingUtilities.invokeLater(() -> {
			new MainWindow(ctrl);  
		});

	}

	private static void start(String[] args) throws Exception {
		initFactories();
		parseArgs(args);
		switch (mode) {
		case BATCH:
			startBatchMode();
			break;
		case GUI:
			startGUIMode();
			break;
		}
	}

	public static void main(String[] args) {
		Utils.RAND.setSeed(2147483647l);
		try {
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}

