package learn.foraging.ui;

import learn.foraging.data.DataException;
import learn.foraging.domain.ForageService;
import learn.foraging.domain.ForagerService;
import learn.foraging.domain.ItemService;
import learn.foraging.domain.Result;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class Controller {

    private final ForagerService foragerService;
    private final ForageService forageService;
    private final ItemService itemService;
    private final View view;

    public Controller(ForagerService foragerService, ForageService forageService, ItemService itemService, View view) {
        this.foragerService = foragerService;
        this.forageService = forageService;
        this.itemService = itemService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Sustainable Foraging");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_FORAGES_BY_DATE:
                    viewByDate();
                    break;
                case VIEW_ITEMS:
                    viewItems();
                    break;
                case ADD_FORAGE:
                    //addForage();
                    addForage();
                    break;
                case ADD_FORAGER:
                    //view.displayStatus(false, "NOT IMPLEMENTED");
                    addForager();
                    view.enterToContinue();
                    break;
                case ADD_ITEM:
                    addItem();
                    break;
                case REPORT_KG_PER_ITEM:
                    //view.displayStatus(false, "NOT IMPLEMENTED");
                    reportKGPerItem();
                    view.enterToContinue();
                    break;
                case REPORT_CATEGORY_VALUE:
                    reportCategoryValue();
                    //view.displayStatus(false, "NOT IMPLEMENTED");
                    view.enterToContinue();
                    break;
                case VIEW_FORAGERS_BY_STATE:
                    viewForagersByState();

                case GENERATE:
                    //generate();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    // top level menu
    private void viewByDate() {
        LocalDate date = view.getForageDate();
        List<Forage> forages = forageService.findByDate(date);
        view.displayForages(forages);
        view.enterToContinue();
    }

    private void viewItems() {
        view.displayHeader(MainMenuOption.VIEW_ITEMS.getMessage());
        Category category = view.getItemCategory();
        List<Item> items = itemService.findByCategory(category);
        view.displayHeader("Items");
        view.displayItems(items);
        view.enterToContinue();
    }

    private void addForage() throws DataException {
        view.displayHeader(MainMenuOption.ADD_FORAGE.getMessage());
        Forager forager = getForager();
        if (forager == null) {
            return;
        }
        Item item = getItem();
        if (item == null) {
            return;
        }
        Forage forage = view.makeForage(forager, item);
        Result<Forage> result = forageService.add(forage);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Forage %s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void addItem() throws DataException {
        Item item = view.makeItem();
        Result<Item> result = itemService.add(item);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Item %s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }

    private void generate() throws DataException {
        GenerateRequest request = view.getGenerateRequest();
        if (request != null) {
            int count = forageService.generate(request.getStart(), request.getEnd(), request.getCount());
            view.displayStatus(true, String.format("%s forages generated.", count));
        }
    }

    // support methods
    private Forager getForager() {
        String lastNamePrefix = view.getForagerNamePrefix();
        List<Forager> foragers = foragerService.findByLastName(lastNamePrefix);
        return view.chooseForager(foragers);
    }

    private Item getItem() {
        Category category = view.getItemCategory();
        List<Item> items = itemService.findByCategory(category);
        return view.chooseItem(items);
    }



    private void viewForagersByState(){
        view.displayHeader(MainMenuOption.VIEW_FORAGERS_BY_STATE.getMessage());
        String stateAbbreviations = view.stateAbbreviations();
        List<Forager> foragers = foragerService.findByState(stateAbbreviations);
        view.displayForagers(foragers);
        view.enterToContinue();
    }


    private void addForager() throws DataException {
        view.displayHeader(MainMenuOption.ADD_FORAGER.getMessage());
        Forager forager = view.makeForager();
        Result<Forager> result = foragerService.add(forager);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = String.format("Forager %s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
    }


    private void reportKGPerItem() {
        LocalDate date = view.getReportKGPerItemDate();
        view.displayHeader(MainMenuOption.REPORT_KG_PER_ITEM.getMessage());
        Map <Item, Double> kgPerItem = forageService.reportKgOfEachItemOnDay(date);
        view.displayKGPerItemReport(kgPerItem);
        view.enterToContinue();
    }

    private void reportCategoryValue(){
        System.out.println("Still Working on it . ");

    }



}

