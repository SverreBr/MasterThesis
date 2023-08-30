# Master Thesis
This is the repository containing the code for my master thesis: The Influence of Lying in a Negotiation Setting: Colored Trails.

## Content
The code consists of two main folders that are located in the folder src > main > java: lyingAgents and results. 
The lyingAgents folder contains the code of the graphical user interface (GUI) and the model of the agents. 
The results folder contains the code for running the experiments.

## Run
The GUI has been tested on Windows using IntelliJ IDEA and Eclipse and Java versions 17 and 19.
Moreover, the experiments were run on a Linux operating system, where we tested the code on Java versions 11 and 17.

In my thesis, I explain the steps to install Eclipse and in detail. Here, I provide an intuition on how to get started.

1. Download the code from this repository, or clone the repository.
2. Place the zip file in the preferred folder and extract all files. You should now have folder that contains at least two subfolders: fig and src.
3. Ensure that src/main/java is labeled as a source folder in your preferred Java editor (in Windows).
4. Open the file Main.java that is located in the folder src/main/java/lyingAgents.
5. You can now run the GUI by running the file Main.java.
6. Might the problem arise that the GUI does not fit your screen, try the following. Go to Settings on your laptop by selecting Start > Settings > System > Display. Then, to change the size of your text and apps, choose an option (either 125\% or 100\% (recommended)) from the drop-down menu next to Scale. Now, the GUI should fit your screen. 

## Acknowledgement
Some of the Java code in this project is adapted from code provided by Harmen de Weerd. 
In particular, the classes Player.java and PlayerToM.java include adapted parts of code provided by Harmen de Weerd. 
Harmen de Weerd used this code, for example, in the following article:

* de Weerd, H., Verbrugge, R., & Verheij, B. (2017). Negotiating with other minds: The role of recursive theory of mind in negotiation with incomplete information. Autonomous Agents and Multi-Agent Systems, 31(2), 250–287.

### Main changes
Apart from some changes in the layout of the code, I changed and added a few functionalities compared to the provided code by Harmen de Weerd. These are:

* Beliefs of ToM_k, k ≥ 1, are not used in the code. I think that only the location beliefs are used, and the acceptance beliefs are used only by the zero-order theory of mind agents. That is, for a first-order theory of mind agent, we do not have to update its first-order beliefs in the code (only the first-order location beliefs), as we only have to update the zero-order beliefs of the trading partner. I have changed this so that fewer updates are made.
* I implemented that an agent only sends offers when the trading partner indeed receives messages. Previously, offers were also sent to the partner model and the self model when an offer is going to be accepted or the agent withdraws from negotiation.
* The fields countBeliefsOfferType and countTotalOfferType were not saved in Player.java. I added this in save beliefs and restore beliefs. Because when you model sending an offer to your trading partner (partner model), you increase these counts, but they are not restored. This might influence some results as the probability of accepting an offer increases for the modeled partner.
* I added an epsilon (10−9) to compare doubles. By doing this more offers are flagged as the best offers (as they have the same value, but due to double comparisons, this was missed).
• I added randomness in offers that are chosen.
• The function GetValue(), i.e. the method to get the expected value of making an offer, is changed to stochastic and not deterministic for ToM_k agents, k ≥ 1. This means that the value of an offer is stochastic. This is caused by the modeled trading partner that chooses the best offer among the list of offers that gives the trading partner itself the highest value, but for the other agent the offers made by the trading partner may have different values. (The random offer choosing was not in the code of Harmen, and therefore it was not stochastic before). I made a new method that returns all possibilities to get a new value and takes the average (as every return offer is equally likely).

## Citation
Please cite the following Thesis when using this code:
