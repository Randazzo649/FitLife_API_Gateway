package com.fitlife.gateway.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.fitlife.gateway", importOptions = ImportOption.DoNotIncludeTests.class)
class GatewayArchitectureTest {
    @ArchTest
    static final ArchRule gateway_nao_depende_de_bffs_ou_mfes = noClasses()
            .should().dependOnClassesThat()
            .resideInAnyPackage("..bff..", "..mfe..");

    @ArchTest
    static final ArchRule pacotes_sem_ciclos = slices()
            .matching("com.fitlife.gateway.(*)..")
            .should().beFreeOfCycles();
}
