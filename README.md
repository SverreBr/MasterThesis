# Master Thesis
This is the repository containing the code for my master thesis: Influence of Lying in a Negotiation Setting: The Colored Trails.

## Content
Explain the content.

## Run
Explain how to run the code.

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
