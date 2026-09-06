# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]

        "*** YOUR CODE HERE ***"
        score = successorGameState.getScore()
        foodList = newFood.asList()
        if foodList:
            minFoodDistance = min([manhattanDistance(newPos, food)for food in foodList])
            score += 10 / (minFoodDistance + 1)
        for i, ghostState in enumerate(newGhostStates):
            ghostpos = ghostState.getPosition()
            distanceToGhost = manhattanDistance(newPos, ghostpos)
            if newScaredTimes[i] > 0:
                score += 200 / (distanceToGhost + 1)
            else:
                if distanceToGhost < 2:
                    score -= 1000
        return score
def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """
        "*** YOUR CODE HERE ***"
        def minimax(agentIndex, depth, gameState):
            if gameState.isWin() or gameState.isLose() or depth == self.depth * gameState.getNumAgents():
                return self.evaluationFunction(gameState), None
            
            if agentIndex == 0:
                return max(
                    (minimax((agentIndex + 1) % gameState.getNumAgents(), depth + 1, gameState.generateSuccessor(agentIndex, action))[0], action)
                    for action in gameState.getLegalActions(agentIndex))
            else:
                return min(
                    (minimax((agentIndex + 1) % gameState.getNumAgents(), depth + 1, gameState.generateSuccessor(agentIndex, action))[0], action)
                    for action in gameState.getLegalActions(agentIndex))

        score, action = minimax(0, 0, gameState)
        return action

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        def alphaBeta(agentIndex, depth, gameState, alpha, beta):
            if gameState.isWin() or gameState.isLose() or depth == self.depth:
                return self.evaluationFunction(gameState)
            if agentIndex == 0:
                return maxvalue(agentIndex, depth, gameState, alpha, beta)
            else:
                return minvalue(agentIndex, depth, gameState, alpha, beta)
        def maxvalue(agentIndex, depth, gameState, alpha, beta):
            actions = gameState.getLegalActions(agentIndex)
            if not actions:
                return self.evaluationFunction(gameState)
            v = float('-inf')
            for action in actions:
                successor =  gameState.generateSuccessor(agentIndex, action)
                v = max(v, alphaBeta(1, depth,successor, alpha, beta))
                if v > beta:
                    return v
                alpha = max(alpha, v)
            return v
        def minvalue(agentIndex, depth, gameState, alpha, beta):
            actions = gameState.getLegalActions(agentIndex)
            if not actions:
                return self.evaluationFunction(gameState)
            v = float('inf')
            nextAgent = (agentIndex + 1) % gameState.getNumAgents()
            if nextAgent == 0:
                nextDepth = depth + 1
            else:
                nextDepth = depth
            for action in actions:
                successor = gameState.generateSuccessor(agentIndex, action)
                v = min(v, alphaBeta(nextAgent, nextDepth, successor, alpha, beta))
                if v < alpha:
                    return v
                beta  = min(beta, v)
            return v
        alpha = float('-inf')
        beta = float('inf')

        actions = gameState.getLegalActions(0)
        bestAction = None
        bestValue = float('-inf')
        for action in actions:
            succssor = gameState.generateSuccessor(0, action)
            value = alphaBeta(1, 0, succssor, alpha, beta)
            if value > bestValue:
                bestValue = value
                bestAction = action
            alpha = max(alpha, value)
        return bestAction
        
class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***"
        def expectimax(agentIndex, depth, gameState):
            if gameState.isWin() or gameState.isLose() or depth == self.depth:
                return self.evaluationFunction(gameState)
            
            if agentIndex == 0:
                return maxvalue(agentIndex, depth, gameState)
            else:
                return expvalue(agentIndex, depth, gameState)
        def maxvalue(agentIndex, depth, gameState):
            actions = gameState.getLegalActions(agentIndex)
            if not actions:
                return self.evaluationFunction(gameState)
            bestvalue = float('-inf')
            for action in actions:
                successor = gameState.generateSuccessor(agentIndex, action)
                bestvalue = max(bestvalue, expectimax(1, depth, successor))
            return bestvalue
        def expvalue(agentIndex, depth, gameState):
            actions = gameState.getLegalActions(agentIndex)
            if not actions:
                return self.evaluationFunction(gameState)
            totalvalue = 0
            prob = 1.0 / len(actions)
            nextAgent = (agentIndex + 1) % gameState.getNumAgents()
            if nextAgent == 0:
                nextDepth = depth + 1
            else:
                nextDepth = depth
            
            for action in actions:
                successor = gameState.generateSuccessor(agentIndex, action)
                totalvalue += prob * expectimax(nextAgent, nextDepth, successor)
            return totalvalue
        actions = gameState.getLegalActions(0)
        bestAction = None
        bestvalue = float('-inf')

        for action in actions:
            successor = gameState.generateSuccessor(0, action)
            value = expectimax(1, 0, successor)
            if value > bestvalue:
                bestvalue = value
                bestAction = action
        return bestAction


def betterEvaluationFunction(currentGameState: GameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <write something here so we know what you did>
    """
    "*** YOUR CODE HERE ***"
    pacmanPos = currentGameState.getPacmanPosition()
    foodList = currentGameState.getFood().asList()
    ghostStates = currentGameState.getGhostStates()
    capsules = currentGameState.getCapsules()

    if currentGameState.isWin():
        return float('inf')
    if currentGameState.isLose():
        return float('-inf')        
    
    score = currentGameState.getScore()
    #food
    if foodList:
        closestFoodDistance = min(
            [manhattanDistance(pacmanPos, foodPos) 
             for foodPos in foodList])
        score += 20.0 / (closestFoodDistance + 1)
    score -= 5.0 * len(foodList)

    score -= 15 * len(capsules) 

    if capsules:
        closestCapsuleDistance = min(
            [manhattanDistance(pacmanPos, capsulePos) 
             for capsulePos in capsules]
        )
        score += 10.0 / (closestCapsuleDistance + 1)

    #ghost
    for ghostState in ghostStates:
        ghostPos = ghostState.getPosition()
        ghostDistance = manhattanDistance(pacmanPos, ghostPos)

        if ghostState.scaredTimer > 0:
            score += 200.0 / (ghostDistance + 1)
        else:
            if ghostDistance <= 1:
                score -= 1000.0
            elif ghostDistance <= 2:
                score -= 200
            else:
                score -= 2.0 / ghostDistance 
    return score 
# Abbreviation
better = betterEvaluationFunction
