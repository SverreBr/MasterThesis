# Master Thesis
This is the repository containing the code for my Master's thesis: The Influence of Lying in a Negotiation Setting: Colored Trails.
The article *Is Honesty the Best Policy in Negotiation? The Influence of Lying in Colored Trails* by Sverre Brok, Rineke Verbrugge, and Harmen de Weerd, accepted for publication in Autonomous Agents and Multi-Agent Systems, reports simulation results of this thesis, which were obtained with this code.

## Content
This repository consists of code for running the experiments (located in *src/main/java/results*), 
code for running the GUI (located in *src/main/java/lyingAgents*), 
and code for making the figures of the results (located in *py_src*). 

## Run
The GUI has been tested on Windows using IntelliJ IDEA and Eclipse and Java versions 17 and 19.
Moreover, the experiments were run on a Linux operating system, where we tested the code on Java versions 11 and 17.
The code does not use any external Java libraries; a Java Development Kit (JDK) of version 17 or newer suffices.

A detailed explanation of how to run the code is given in my Master's thesis:
```
Brok, S. (2023, August). The influence of lying in a negotiation setting: Colored Trails [Master’s thesis]. (Available at https://fse.studenttheses.ub.rug.nl/)
```
I explain the steps to install Eclipse and in detail. Here, I provide an intuition on how to get started.

1. Download the code from this repository, or clone the repository.
2. Place the zip file in the preferred folder and extract all files. You should now have folder that contains at least two subfolders: fig and src.
3. Ensure that src/main/java is labeled as a source folder in your preferred Java editor (in Windows).
4. Open the file Main.java that is located in the folder src/main/java/lyingAgents.
5. You can now run the GUI by running the file Main.java.
6. Might the problem arise that the GUI does not fit your screen, try the following. Go to Settings on your laptop by selecting Start > Settings > System > Display. Then, to change the size of your text and apps, choose an option (either 125\% or 100\% (recommended)) from the drop-down menu next to Scale. Now, the GUI should fit your screen. 

Without a Java editor, the GUI can be compiled and started from the root folder of this repository (the folder that contains *fig* and *src*) with:
```
javac --source-path src/main/java -d bin src/main/java/lyingAgents/Main.java
java -cp bin lyingAgents.Main
```

## Run the experiments
The experiments do not need a screen. Compile and run them from the root folder of this repository:
```
javac --source-path src/main/java -d bin src/main/java/results/MainResults.java
java -cp bin results.MainResults -experiment main -file_id 1
```
The options are:

| Option | Meaning | Default |
|---|---|---|
| `-experiment <name>` | The experiment to run (see the table below). | `main` |
| `-file_id <id>` | The output file is called `results_<id>.csv`. Use letters, digits, `-`, `_` and `.` only. | output file `results.csv` |
| `-h`, `--help` | Print the options and exit. | |

One run of `MainResults` performs **one repetition** of an experiment.
For every pair of agents in the experiment, a new initiator and a new responder are created, they play 100 warm-up negotiations (not recorded), and then one negotiation is recorded.
Each recorded negotiation is one row in the file `tmp_results/<experiment>/results_<id>.csv`.
Every run draws new boards, chips, and goal locations; there is no random seed, so two runs give different, but statistically equivalent, results.

| Experiment | Thesis | Article | Pairs of agents (rows per run) |
|---|---|---|---|
| `determine_prob` | Experiment 1 (setting the probability *p*<sub>0</sub>) | the separate experiment on *p*<sub>0</sub> in Section 3.5 | 99 |
| `determine_prob_compare` | Section 4.1 (fraction of offers with a goal location message, *p*<sub>0</sub> = 0) | – | 24 |
| `main` | Experiment 2 (lying and misleading versus honesty) | Experiment 1 | 25 |
| `de_weerd` | Experiment 3 (agents of De Weerd et al. (2017), without goal location messages) | Experiment 2 | 9 |
| `lr` | Experiment 4 (varying the learning speed λ) | – | 225 |
| `pareto` | Experiment 5 (only games in which a Pareto improvement exists) | – | 25 |

## Repeating the experiments 1000 times (Hábrók)
In the thesis and in the article, every experiment was repeated 1000 times.
We ran these repetitions on the Hábrók high-performance computing cluster of the University of Groningen (Center for Information Technology, 2023, https://wiki.hpc.rug.nl/habrok/start), which uses a Linux operating system.
Each job ran `MainResults` once, that is, one repetition, with its own `-file_id`, so that every job wrote its own file in `tmp_results/<experiment>/`.
The analysis notebooks read all `.csv` files in a folder and combine them.

On a single computer, the same can be done with a loop (bash), for example:
```bash
for id in $(seq 1 1000); do
    java -cp bin results.MainResults -experiment de_weerd -file_id $id
done
```

On a cluster that uses Slurm, such as Hábrók, a job array does the same.
The script below is an example; it is not the original job script.
The name of the Java module and the other job settings, such as a time limit, depend on the cluster (see its documentation).
```bash
#!/bin/bash
#SBATCH --job-name=ct_main
#SBATCH --array=1-1000
#SBATCH --ntasks=1

module load Java   # see the documentation of the cluster for the exact module name
java -cp bin results.MainResults -experiment main -file_id ${SLURM_ARRAY_TASK_ID}
```

## Output
Every row of a results file describes one recorded negotiation. The columns are:

| Column | Meaning |
|---|---|
| `init_tom`, `resp_tom` | Order of theory of mind of the initiator and the responder (0, 1, or 2). |
| `init_lr`, `resp_lr` | Learning speed λ of the initiator and the responder. |
| `init_canSendMessages`, `resp_canSendMessages` | Whether the agent can send goal location messages. |
| `init_canLie`, `resp_canLie` | Whether the agent can send false goal location messages (misleading ToM<sub>1</sub>, lying ToM<sub>2</sub>; always true for a ToM<sub>0</sub> agent that sends messages). |
| `init_initPoints`, `resp_initPoints` | Score with the initial chips. |
| `init_finalPoints`, `resp_finalPoints` | Score with the chips after the negotiation. |
| `init_gain`, `resp_gain` | Score gain: final score minus initial score. The cost of one point per offer is not subtracted. |
| `init_canInitiallyReachGP`, `resp_canInitiallyReachGP` | Whether the agent can reach its goal location with its initial chips (games in which an agent can are not recorded, so this is false in recorded games). |
| `init_zeroToMProb`, `resp_zeroToMProb` | Probability *p*<sub>0</sub> that a ToM<sub>0</sub> agent sends a goal location message with an offer. The value is stored for every agent, but only used by ToM<sub>0</sub> agents that can send messages. |
| `init_numberOfMessagesSent`, `resp_numberOfMessagesSent` | Number of goal location messages the agent sent. |
| `init_numberOfTimesLied`, `resp_numberOfTimesLied` | Number of goal location messages that contain a goal location other than the agent's own (false messages; for a ToM<sub>0</sub> agent these are random, for a ToM<sub>1</sub> agent misleading, for a ToM<sub>2</sub> agent lies). |
| `init_highestValueParetoOutcome`, `resp_highestValueParetoOutcome` | Highest score of the agent among the chip distributions that are a Pareto improvement over the initial distribution (no agent worse off, at least one agent better off); −1 if there is none. |
| `init_highestValueStrictParetoOutcome`, `resp_highestValueStrictParetoOutcome` | The same for strict Pareto improvements (both agents better off). |
| `init_nrOfFinalChips`, `resp_nrOfFinalChips` | Number of chips of the agent after the negotiation. |
| `highest_SW_strict_PE`, `highest_SW_PE` | Highest social welfare (sum of both scores) among the strict Pareto improvements and among the Pareto improvements; −1 if there is none. |
| `nr_offers` | Number of offers made in the negotiation (at most 100, see `reached_max_num_offers`). |
| `outcome_is_StrictPE` | True if no strict Pareto improvement gives both agents a higher score than the final distribution (also true if there is no strict Pareto improvement). |
| `outcome_is_PE` | The same for Pareto improvements. |
| `outcome_is_highestSW` | True if the social welfare of the final distribution equals the highest social welfare among the strict Pareto improvements (also true if there is none). |
| `outcome_is_new_distribution` | True if the final distribution differs from the initial distribution. |
| `there_is_better_outcome_than_initial_situ` | True if a strict Pareto improvement over the initial distribution exists. |
| `reached_max_num_offers` | True if the negotiation was stopped after 100 offers; the initial distribution then becomes final. |
| `passed_time` | Seconds used for this pair of agents, including the warm-up negotiations. |

## Analysis notebooks
The folder *py_src* contains the Jupyter notebooks for the experiment on *p*<sub>0</sub> (`expDetermineProb.ipynb`) and for the fraction of offers with a goal location message (`expDetermineProbCompare.ipynb`).
The notebooks were run with Python 3.9.7; the packages are listed in *py_src/requirements.txt*, and Jupyter is needed to open the notebooks.
```
pip install -r py_src/requirements.txt jupyter
```
The notebooks read every `.csv` file in the folder `results/results_<ID>_<experiment>/` in the root of this repository, where `ID` is set at the top of each notebook (`"0704"`).
So, before running `expDetermineProb.ipynb`, copy the files from `tmp_results/determine_prob/` to `results/results_0704_determine_prob/`, and before running `expDetermineProbCompare.ipynb`, copy the files from `tmp_results/determine_prob_compare/` to `results/results_0704_determine_prob_compare/`.
The figures are saved in *py_src/figs/&lt;ID&gt;/*.

The code that made the figures and the statistical tests for Experiments 1 and 2 of the article is not part of this repository.

## Acknowledgement
Some of the Java code in this project is adapted from code provided by Harmen de Weerd. 
In particular, the classes Player.java and PlayerToM.java include adapted parts of code provided by Harmen de Weerd. 
Harmen de Weerd used this code, for example, in the following article:
```
de Weerd, H., Verbrugge, R., & Verheij, B. (2017). Negotiating with other minds: The role of recursive theory of mind in negotiation with incomplete information. Autonomous Agents and Multi-Agent Systems, 31(2), 250–287.
```

The computational resources for this study were made possible by the Center for Information Technology of the University of Groningen, which granted access to the Hábrók high-performance computing cluster.

### Main changes
Apart from some changes in the layout of the code, I changed and added a few functionalities compared to the provided code by Harmen de Weerd. These are:

* Beliefs of ToM_k, k ≥ 1, are not used in the code. I think that only the location beliefs are used, and the acceptance beliefs are used only by the zero-order theory of mind agents. That is, for a first-order theory of mind agent, we do not have to update its first-order beliefs in the code (only the first-order location beliefs), as we only have to update the zero-order beliefs of the trading partner. I have changed this so that fewer updates are made.
* I implemented that an agent only sends offers when the trading partner indeed receives messages. Previously, offers were also sent to the partner model and the self model when an offer is going to be accepted or the agent withdraws from negotiation.
* The fields countBeliefsOfferType and countTotalOfferType were not saved in Player.java. I added this in save beliefs and restore beliefs. Because when you model sending an offer to your trading partner (partner model), you increase these counts, but they are not restored. This might influence some results as the probability of accepting an offer increases for the modeled partner.
* I added an epsilon (10−9) to compare doubles. By doing this more offers are flagged as the best offers (as they have the same value, but due to double comparisons, this was missed).
* I added randomness in offers that are chosen.
* The function GetValue(), i.e. the method to get the expected value of making an offer, is changed to stochastic and not deterministic for ToM_k agents, k ≥ 1. This means that the value of an offer is stochastic. This is caused by the modeled trading partner that chooses the best offer among the list of offers that gives the trading partner itself the highest value, but for the other agent the offers made by the trading partner may have different values. (The random offer choosing was not in the code of Harmen, and therefore it was not stochastic before). I made a new method that returns all possibilities to get a new value and takes the average (as every return offer is equally likely).

## Citation
Please use the following citation when using this code:
```
Brok, S. (2023, August). The influence of lying in a negotiation setting: Colored Trails [Master’s thesis]. (Available at https://fse.studenttheses.ub.rug.nl/)
```

Or, use the following bib entry:
```
@misc{mythesis,
    title        = {The influence of lying in a negotiation setting: {C}olored {T}rails},
    author       = {Sverre Brok},
    year         = {2023},
    month        = {August},
    note         = {Available at \url{https://fse.studenttheses.ub.rug.nl/}},
    school       = {University of Groningen},
    type         = {Master's thesis}
}
```

The article that reports simulation results of the thesis:
```
Brok, S., Verbrugge, R., & de Weerd, H. (in press). Is honesty the best policy in negotiation? The influence of lying in Colored Trails. Autonomous Agents and Multi-Agent Systems.
```

## License
This code is available under the MIT License; see the file *LICENSE*.
